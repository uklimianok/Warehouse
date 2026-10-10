package com.warehouse.demo.flow.product;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.warehouse.demo.AbstractIT;
import com.warehouse.demo.flow.setupData.SetupData;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductPalletFlowIT extends AbstractIT {
    private static final String SYSTEM_ADMINISTRATOR = "24000001";
    private static final String DATA_CONTROLLER = "09000001";
    private static final String OPERATOR = "06000001";
    private static final String GOODS_UNLOADER = "02000001";
    private static final String GOODS_PICKER = "03000001";
    private static final String COORDINATOR = "08000001";

    @Test
    void palletIsCreatedAsOrdered() {
        String token = tokenFor("24000001", TEST_PASSWORD);
        SetupData data = createSetupData(token);

        long orderedId = getIdByName("/statuses", "Ordered", token);

        long ppId = create("/product-pallets", Map.of(
            "productPackageId", data.packageId(),
            "packageAmount", 40,
            "palletId", data.palletId(),
            "groupNumber", "G" + System.nanoTime(),
            "statusId", orderedId
        ), token);

        client.get()
            .uri("/product-pallets/" + ppId)
            .header("Authorization", "Bearer " + token)
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.status.name").isEqualTo("Ordered");
    }

    @Test 
    void palletGoesThroughReceivingFlow() {
        String sysAdminToken = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);
        String dataControllerToken = tokenFor(DATA_CONTROLLER, TEST_PASSWORD);
        String operatorToken = tokenFor(OPERATOR, TEST_PASSWORD);
        String goodsUnloaderToken = tokenFor(GOODS_UNLOADER, TEST_PASSWORD);

        SetupData dataA = createSetupData(sysAdminToken);
        long orderedId = getIdByName("/statuses", "Ordered", sysAdminToken);

        long ppId = create("/product-pallets", palletBody(dataA, orderedId, null, null), sysAdminToken);

        putPallet(ppId, palletBody(dataA, orderedId, null, dataA.stationId()), sysAdminToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation").isEqualTo(null)
            .jsonPath("$.nextWorkStation.id").isEqualTo(dataA.stationId())
            .jsonPath("$.status.name").isEqualTo("Ordered");

        long unloadedId = getIdByName("/statuses", "Unloaded", sysAdminToken);

        putPallet(ppId, palletBody(dataA, unloadedId, dataA.stationId(), null), goodsUnloaderToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation.id").isEqualTo(dataA.stationId())
            .jsonPath("$.nextWorkStation").isEqualTo(null);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Unloaded");

        SetupData dataB = createSetupData(sysAdminToken);

        putPallet(ppId, palletBody(dataA, unloadedId, dataA.stationId(), dataB.stationId()), dataControllerToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation.id").isEqualTo(dataA.stationId())
            .jsonPath("$.nextWorkStation.id").isEqualTo(dataB.stationId());

        long storedId = getIdByName("/statuses", "Stored", sysAdminToken);

        putPallet(ppId, palletBody(dataA, storedId, dataA.stationId(), dataB.stationId()), operatorToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation.id").isEqualTo(dataB.stationId())
            .jsonPath("$.nextWorkStation").isEqualTo(null);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Stored");

        SetupData dataC = createSetupData(sysAdminToken);

        putPallet(ppId, palletBody(dataA, storedId, dataB.stationId(), dataC.stationId()), dataControllerToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation.id").isEqualTo(dataB.stationId())
            .jsonPath("$.nextWorkStation.id").isEqualTo(dataC.stationId());

        long activeId = getIdByName("/statuses", "Active", sysAdminToken);
            
        putPallet(ppId, palletBody(dataA, activeId, dataC.stationId(), null), operatorToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation.id").isEqualTo(dataC.stationId())
            .jsonPath("$.nextWorkStation").isEqualTo(null);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Active");

        long outOfUseId = getIdByName("/statuses", "Out-of-use", sysAdminToken);

        putPallet(ppId, palletBody(dataA, outOfUseId, null, null), operatorToken)
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.workStation").isEqualTo(null)
            .jsonPath("$.nextWorkStation").isEqualTo(null);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Out-of-use");
    }

    @Test 
    void cachedPalletKeepsItsRelations() {
        String sysAdminToken = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);
        SetupData data = createSetupData(sysAdminToken);
        long orderedId = getIdByName("/statuses", "Ordered", sysAdminToken);

        long ppId = create("/product-pallets", palletBody(data, orderedId, null, null), sysAdminToken);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Ordered");

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo("Ordered");
    }

    @ParameterizedTest(name = "{1}: {0} -> {2} = {3}")
    @CsvSource({
        "Ordered, " + GOODS_UNLOADER + ", Stored, 403", // unloader skips a step
        "Ordered, " + OPERATOR + ", Unloaded, 403",     // wrong position for this step
        "Unloaded, " + GOODS_UNLOADER + ", Stored, 403",// unloader can't store
        "Stored, " + GOODS_UNLOADER + ", Active, 403",
        "Active, " + OPERATOR + ", Stored, 403",        // backwards
        "Ordered, " + OPERATOR + ", Out-of-use, 403",   // skips everything
        "Ordered, " + GOODS_PICKER + ", Unloaded, 403", // no U flag at all
        "Active, " + COORDINATOR + ", Ordered, 200"     // allowed reset (your rule)
    })
    void palletStatusRules(String start, String employee, String target, int http) {
        String sysAdminToken = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);
        String roleToken = tokenFor(employee, TEST_PASSWORD);

        SetupData dataA = createSetupData(sysAdminToken);
        SetupData dataB = createSetupData(sysAdminToken);

        long statusId = getIdByName("/statuses", target, sysAdminToken);

        long ppId = palletInStatus(start, dataA, sysAdminToken);

        Long workStationId = null;
        Long nextWorkStationId = null;

        switch (target) {
            case "Unloaded", "Active": 
                workStationId = dataA.stationId(); 
                break;
            case "Stored": {
                workStationId = dataA.stationId();
                nextWorkStationId = dataB.stationId();
            } break;
        
            default:
                break;
        }

        Map<String, Object> body = palletBody(dataA, statusId, workStationId, nextWorkStationId);

        putPallet(ppId, body, roleToken)
            .expectStatus().isEqualTo(http);

        client.get()
            .uri("/product-pallets/{id}", ppId)
            .header("Authorization", "Bearer " + sysAdminToken)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo(http == 200 ? target : start);
    }

    @ParameterizedTest(name = "{0} sees {1}")
    @CsvSource(
        delimiter = '|',
        value = {
            "03000001 | id,productPackage,palletNumber,groupNumber,workStation",
            "02000001 | id,productPackage,palletNumber,groupNumber,workStation,pallet,nextWorkStation",
            "08000001 | id,productPackage,palletNumber,groupNumber,workStation,pallet,nextWorkStation,packageAmount,status"
        }
    )
    void responseShapeDependsOnPosition(String employee, String expectedKeys) {
        String sysAdminToken = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);
        String roleToken = tokenFor(employee, TEST_PASSWORD);


        SetupData data = createSetupData(sysAdminToken);
        long productPalletId = palletInStatus("Ordered", data, sysAdminToken);

        Map<String, Object> body = client.get()
            .uri("/product-pallets/{id}", productPalletId)
            .headers(h -> h.setBearerAuth(roleToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {})
            .returnResult().getResponseBody();

        assertThat(body.keySet())
            .containsExactlyInAnyOrder(expectedKeys.split(","));
    }

    private Map<String, Object> palletBody(SetupData data, long statusId, Long workStationId, Long nextWorkStationId) {
        Map<String, Object> body = new HashMap<>();
        body.put("productPackageId", data.packageId());
        body.put("packageAmount", 40);
        body.put("palletId", data.palletId());
        body.put("groupNumber", "G1");
        body.put("statusId", statusId);
        body.put("workStationId", workStationId);
        body.put("nextWorkStationId", nextWorkStationId);
        return body;
    }

    private RestTestClient.ResponseSpec putPallet(long id, Map<String, Object> body, String token) {
        return client.put()
            .uri("/product-pallets/{id}", id)
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange();
    }

    private long palletInStatus(String statusName, SetupData data, String sysAdminToken) {
        SetupData dataA = createSetupData(sysAdminToken);
        long orderedId = getIdByName("/statuses", "Ordered", sysAdminToken);

        long ppId = create("/product-pallets", palletBody(data, orderedId, null, null), sysAdminToken);

        putPallet(ppId, palletBody(data, orderedId, null, dataA.stationId()), sysAdminToken)
            .expectStatus().isOk();

        if (statusName.equals("Ordered")) return ppId;

        long unloadedId = getIdByName("/statuses", "Unloaded", sysAdminToken);

        putPallet(ppId, palletBody(data, unloadedId, dataA.stationId(), null), sysAdminToken)
            .expectStatus().isOk();

        if (statusName.equals("Unloaded")) return ppId;

        SetupData dataB = createSetupData(sysAdminToken);

        putPallet(ppId, palletBody(data, unloadedId, dataA.stationId(), dataB.stationId()), sysAdminToken)
            .expectStatus().isOk();

        long storedId = getIdByName("/statuses", "Stored", sysAdminToken);

        putPallet(ppId, palletBody(data, storedId, dataA.stationId(), dataB.stationId()), sysAdminToken)
            .expectStatus().isOk();

        SetupData dataC = createSetupData(sysAdminToken);

        putPallet(ppId, palletBody(data, storedId, dataB.stationId(), dataC.stationId()), sysAdminToken)
            .expectStatus().isOk();

        if (statusName.equals("Stored")) return ppId;

        long activeId = getIdByName("/statuses", "Active", sysAdminToken);
            
        putPallet(ppId, palletBody(data, activeId, dataC.stationId(), null), sysAdminToken)
            .expectStatus().isOk();

        if (statusName.equals("Active")) return ppId;

        long outOfUseId = getIdByName("/statuses", "Out-of-use", sysAdminToken);

        putPallet(ppId, palletBody(data, outOfUseId, null, null), sysAdminToken)
            .expectStatus().isOk();

        return ppId;
    }
}
