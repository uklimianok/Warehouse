package com.warehouse.demo.flow.order;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.test.web.servlet.client.RestTestClient.ResponseSpec;

import com.warehouse.demo.AbstractIT;
import com.warehouse.demo.flow.setupData.SetupData;

public class OrderFlowIT extends AbstractIT {
    private static final String SYSTEM_ADMINISTRATOR = "24000001";
    private static final String ORDERS_PROCEEDER = "19000001";
    private static final String GOODS_PICKER = "03000001";
    private static final String SET_GOODS_EXPORTER = "04000001";
    private static final String SET_GOODS_LOADER = "05000001";
    private static final String COORDINATOR = "08000001";

    String sysAdminToken = null;

    private record OrderData(long orderId, long storeId, long shiftId, long gateId) {}

    @BeforeEach 
    void init() {
        sysAdminToken = tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD);
    }

    @Test 
    void orderGoesThroughFulfillmentFlow() {
        String goodsPickerToken = tokenFor(GOODS_PICKER, TEST_PASSWORD);
        String setGoodsExpToken = tokenFor(SET_GOODS_EXPORTER, TEST_PASSWORD);
        String setGoodsLoadToken = tokenFor(SET_GOODS_LOADER, TEST_PASSWORD);
        String coordinatorToken = tokenFor(COORDINATOR, TEST_PASSWORD);

        SetupData data = createSetupData(sysAdminToken);

        long orderId = createAcceptedOrder(data, sysAdminToken).orderId;

        long pickingOrderPalletId = getIdBy("/statuses", "name", "Picking", sysAdminToken);

        long orderPalletId = create("/order-pallets", Map.of("orderId", orderId, "palletId", data.palletId(), "statusId", pickingOrderPalletId), goodsPickerToken);

        assertStatus("/order-pallets/{id}", orderPalletId, "Picking");
        assertStatus("/orders/{id}", orderId, "Processing");

        create("/picked-products", Map.of("orderPalletId", orderPalletId, "packageId", data.packageId(), "pickedVolume", 10, "completed", true), goodsPickerToken);

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Picked", goodsPickerToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Picked");
        assertStatus("/orders/{id}", orderId, "Completed");

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Exporting", setGoodsExpToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Exporting");

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Loading", setGoodsLoadToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Loading");

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Sent", coordinatorToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Sent");
        assertStatus("/orders/{id}", orderId, "Sent");
    }

    @ParameterizedTest(name = "{1}: {0} -> {2} = {3}")
    @CsvSource({
        "Picking, " + SET_GOODS_EXPORTER + ", Exporting, 403",  // exporter, but pallet isn't Picked yet
        "Picked, " + GOODS_PICKER + ", Exporting, 403",         // wrong position
        "Picked, " + SET_GOODS_LOADER + ", Loading, 403",       // loader skips Exporting
        "Exporting, " + SET_GOODS_EXPORTER + ", Loading, 403",  // exporter can't load
        "Loading, " + SET_GOODS_LOADER + ", Sent, 403",         // only Auxiliary/sysadmin can send
        "Picking, " + COORDINATOR + ", Sent, 403",              // Sent requires Loading, even for Auxiliary
        "Picked, " + GOODS_PICKER + ", Picking, 403",           // picker can't go back
        "Loading, " + COORDINATOR + ", Sent, 200",              // the allowed one
        "Picking, " + COORDINATOR + ", Loading, 403"            // Jump over several statuses are restricted
    })
    public void orderPalletStatusRules(String startStatusName, String positionCodeName, String targetStatusName, int expectedCode) {
        SetupData data = createSetupData(sysAdminToken);
        long orderId = createAcceptedOrder(data, sysAdminToken).orderId;

        String roleToken = tokenFor(positionCodeName, TEST_PASSWORD);

        long orderPalletId = orderPalletInStatus(startStatusName, orderId, data);

        moveOrderPallet(orderPalletId, orderId, data.palletId(), targetStatusName, roleToken)
            .expectStatus().isEqualTo(expectedCode);
        
        assertStatus("/order-pallets/{id}", orderPalletId, expectedCode == 200 ? targetStatusName : startStatusName);
    }

    @Test
    void addingPalletToSentOrderReopensIt() {
        String goodsPickerToken = tokenFor(GOODS_PICKER, TEST_PASSWORD);

        SetupData data = createSetupData(sysAdminToken);
        long orderId = createAcceptedOrder(data, sysAdminToken).orderId;
        long orderPalletAId = orderPalletInStatus("Sent", orderId, data);

        assertStatus("/order-pallets/{id}", orderPalletAId, "Sent");

        long pickingStatusId = getIdBy("/statuses", "name", "Picking", sysAdminToken);

        client.post()
            .uri("/order-pallets")
            .headers(h -> h.setBearerAuth(goodsPickerToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "orderId", orderId, 
                "palletId", data.palletId(), 
                "statusId", pickingStatusId
            ))
            .exchange()
            .expectStatus().isCreated();

        assertStatus("/orders/{id}", orderId, "Processing");
    }

    @Test
    void pickerCannotFinishPalletWithIncompleteProduct() {
        String goodsPickerToken = tokenFor(GOODS_PICKER, TEST_PASSWORD);

        SetupData data = createSetupData(sysAdminToken);
        long orderId = createAcceptedOrder(data, sysAdminToken).orderId;
        long orderPalletId = orderPalletInStatus("Picking", orderId, data);

        create("/picked-products", Map.of(
            "orderPalletId", orderPalletId,
            "packageId", data.palletId(),
            "pickedVolume", "10.0",
            "completed", false
        ), sysAdminToken);

        moveOrderPallet(orderPalletId, orderId, data.packageId(), "Picked", goodsPickerToken)
            .expectStatus().isForbidden();

        assertStatus("/order-pallets/{id}", orderPalletId, "Picking");
    }

    @Test 
    void orderIsIncompletedWhenAProductIsNotPicked() {
        String goodsPickerToken = tokenFor(GOODS_PICKER, TEST_PASSWORD);

        SetupData dataA = createSetupData(sysAdminToken);
        SetupData dataB = createSetupData(sysAdminToken);

        long orderId = createAcceptedOrder(dataA, sysAdminToken).orderId;

        client.post()
            .uri("/ordered-products")
            .headers(h -> h.setBearerAuth(sysAdminToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "orderId", orderId,
                "packageId", dataB.packageId(),
                "orderedVolume", "10"
            ))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class)
            .returnResult()
            .getResponseBody();

        long orderPalletId = orderPalletInStatus("Picking", orderId, dataA);

        moveOrderPallet(orderPalletId, orderId, dataA.palletId(), "Picked", goodsPickerToken)
            .expectStatus().isOk();

        assertStatus("/orders/{id}", orderId, "Incompleted");
    }

    @Test
    void proceederCanEditAcceptedOrder() {
        String ordersProcToken = tokenFor(ORDERS_PROCEEDER, TEST_PASSWORD);

        SetupData data = createSetupData(sysAdminToken);
        OrderData order = createAcceptedOrder(data, ordersProcToken);

        putOrder(order, "Accepted", data.gateId(), "new note", ordersProcToken)
            .expectStatus().isOk();

        client.get()
            .uri("/orders/{id}", order.orderId)
            .headers(h -> h.setBearerAuth(ordersProcToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.note").isEqualTo("new note");
    }

    @ParameterizedTest(name = "{1}: {0} -> {2} (gate {3}) = {4}")
    @CsvSource({
        "Accepted, " + ORDERS_PROCEEDER + ", Accepted, true, 200",      // proceeder edits Accepted
        "Processing, " + ORDERS_PROCEEDER + ", Processing, true,  403", // ...but only Accepted
        "Accepted, " + COORDINATOR + ", Accepted, true,  200",          // Auxiliary edits Accepted
        "Processing, " + COORDINATOR + ", Accepted, true, 403",         // back to Accepted: order has pallets
        "Processing, " + COORDINATOR + ", Processing, true, 200",       // status unchanged
        "Accepted, " + GOODS_PICKER + ", Accepted, true, 403",          // Warehouse employee edits
        "Completed, " + SYSTEM_ADMINISTRATOR + ", Sent, true, 200",     // sysadmin may set it manually
        "Accepted, " + SYSTEM_ADMINISTRATOR + ", Processing, false, 409", // no gate after Accepted
        "Sent, " + COORDINATOR + ", Sent, true, 403",   // sent orders are frozen
    })
    void orderStatusRules(String start, String employee, String target, boolean withGate, int http) {
        SetupData data = createSetupData(sysAdminToken);

        OrderData order = orderInStatus(start, data);
        putOrder(order, target, withGate ? order.gateId() : null, "note", tokenFor(employee, TEST_PASSWORD))
            .expectStatus().isEqualTo(http);

        assertStatus("/orders/{id}", order.orderId(), http == 200 ? target : start);
    }

    private OrderData createAcceptedOrder(SetupData data, String token) {
        long n = System.nanoTime();
        long typeId = create("/organization-types", Map.of("name", "Producer" + n), sysAdminToken);
        long storeId = create("/organizations", Map.of("name", "Producer " + n, "organizationNumber", "P" + n, "organizationTypeId", typeId), sysAdminToken);
        long shiftId = getIdBy("/shifts", "symbol", "1", sysAdminToken);
        long acceptedId = getIdBy("/statuses", "name", "Accepted", sysAdminToken);

        long orderId = create("/orders", Map.of("storeId", storeId, "shiftId", shiftId, "gateId", data.gateId(), "statusId", acceptedId), token);

        OrderData order = new OrderData(orderId, storeId, shiftId, data.gateId());

        create("/ordered-products", Map.of("orderId", orderId, "packageId", data.packageId(), "orderedVolume", "10"), token);

        client.get()
            .uri("/orders/{id}", orderId)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(orderId)
            .jsonPath("$.status.name").isEqualTo("Accepted");
        
        return order;
    }

    private RestTestClient.ResponseSpec moveOrderPallet(long orderPalletId, long orderId, long palletId, String statusName, String token) {
        long statusId = statusId(statusName, "Order pallet", tokenFor(SYSTEM_ADMINISTRATOR, TEST_PASSWORD));

        return client.put()
            .uri("/order-pallets/{id}", orderPalletId)
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of(
                "orderId", orderId,
                "palletId", palletId,
                "statusId", statusId
            ))
            .exchange();
    }

    private void assertStatus(String url, long id, String expected) {
        client.get()
            .uri(url, id)
            .headers(h -> h.setBearerAuth(sysAdminToken))
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status.name").isEqualTo(expected);
    }

    private long orderPalletInStatus(String status, long orderId, SetupData data) {
        long pickingOrderPalletId = getIdBy("/statuses", "name", "Picking", sysAdminToken);

        long orderPalletId = create("/order-pallets", Map.of("orderId", orderId, "palletId", data.palletId(), "statusId", pickingOrderPalletId), sysAdminToken);

        assertStatus("/order-pallets/{id}", orderPalletId, "Picking");
        assertStatus("/orders/{id}", orderId, "Processing");

        create("/picked-products", Map.of("orderPalletId", orderPalletId, "packageId", data.packageId(), "pickedVolume", 10, "completed", true), sysAdminToken);

        if (status.equals("Picking")) return orderPalletId;

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Picked", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Picked");
        assertStatus("/orders/{id}", orderId, "Completed");

        if (status.equals("Picked")) return orderPalletId;

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Exporting", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Exporting");

        if (status.equals("Exporting")) return orderPalletId;

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Loading", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Loading");

        if (status.equals("Loading")) return orderPalletId;

        moveOrderPallet(orderPalletId, orderId, data.palletId(), "Sent", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/order-pallets/{id}", orderPalletId, "Sent");
        assertStatus("/orders/{id}", orderId, "Sent");

        return orderPalletId;
    }

    private ResponseSpec putOrder(OrderData order, String statusName, Long gateId, String note, String token) {
        long statusId = statusId(statusName, "Order", sysAdminToken);

        Map<String, Object> body = new HashMap<>();
        body.put("storeId", order.storeId);
        body.put("gateId", gateId);
        body.put("shiftId", order.shiftId);
        body.put("statusId", statusId);
        body.put("note", note);

        return client.put()
            .uri("/orders/{id}", order.orderId)
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange();
    }

    private OrderData orderInStatus(String status, SetupData data) {
        OrderData order = createAcceptedOrder(data, sysAdminToken);

        assertStatus("/orders/{id}", order.orderId(), "Accepted");

        if (status.equals("Accepted")) return order;

        long orderPalletId = orderPalletInStatus("Picking", order.orderId, data);

        assertStatus("/orders/{id}", order.orderId(), "Processing");

        if (status.equals("Processing")) return order;

        create("/picked-products", Map.of("orderPalletId", orderPalletId, "packageId", data.packageId(), "pickedVolume", 10, "completed", true), sysAdminToken);

        moveOrderPallet(orderPalletId, order.orderId(), data.palletId(), "Picked", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/orders/{id}", order.orderId(), "Completed");

        if (status.equals("Completed")) return order;

        moveOrderPallet(orderPalletId, order.orderId(), data.palletId(), "Exporting", sysAdminToken)
            .expectStatus().isOk();

        moveOrderPallet(orderPalletId, order.orderId(), data.palletId(), "Loading", sysAdminToken)
            .expectStatus().isOk();

        moveOrderPallet(orderPalletId, order.orderId(), data.palletId(), "Sent", sysAdminToken)
            .expectStatus().isOk();

        assertStatus("/orders/{id}", order.orderId(), "Sent");

        return order;
    }
}
