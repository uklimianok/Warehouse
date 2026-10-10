package com.warehouse.demo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

import com.warehouse.demo.flow.setupData.SetupData;

import dasniko.testcontainers.keycloak.KeycloakContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {
    @LocalServerPort 
    private int port;

    protected RestTestClient client;

    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");
    static final KafkaContainer kafka = new KafkaContainer("apache/kafka:4.2.2");

    @SuppressWarnings("resource")
    static final GenericContainer<?> redis = new GenericContainer<>("redis:8").withExposedPorts(6379);

    @SuppressWarnings("resource")
    static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.7.4").withRealmImportFile("/keycloak/warehouse-realm.json");

    protected static final String TEST_PASSWORD = "test-password";

    static {
        postgres.start();
        redis.start();
        kafka.start();
        keycloak.start();
    }

    @BeforeEach 
    void setUpClient() {
        client = RestTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    @DynamicPropertySource 
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "/realms/warehouse");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "/realms/warehouse/protocol/openid-connect/certs");
        registry.add("spring.security.oauth2.client.provider.keycloak.token-uri", () -> keycloak.getAuthServerUrl() + "/realms/warehouse/protocol/openid-connect/token");
        registry.add("warehouse.keycloak.admin-url", () -> keycloak.getAuthServerUrl() + "/admin/realms/warehouse");
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret", () -> "test-client-secret");
        registry.add("warehouse.shared-password", () -> "test-password");
    }

    protected String tokenFor(String employeeNumber) {
        return tokenFor(employeeNumber, null);
    }

    protected String tokenFor(String employeeNumber, String password) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", "warehouse-test");
        form.add("username", employeeNumber);
        if (password != null) 
            form.add("password", password);

        Map<?, ?> response = RestClient.create()
            .post()
            .uri(keycloak.getAuthServerUrl() + "/realms/warehouse/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(Map.class);

        return (String) response.get("access_token");
    }

    protected long getIdBy(String url, String key, Object value, String token) {
        List<Map<String, Object>> items = client.get()
            .uri(url)
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
            .returnResult()
            .getResponseBody();

        return items.stream()
            .filter(item -> value.equals(item.get(key)))
            .map(item -> ((Number) item.get("id")).longValue())
            .findFirst()
            .orElseThrow(() -> new AssertionError("No item named '" + value + "' at " + url));
    }

    protected long getIdByName(String url, String name, String token) {
        return getIdBy(url, "name", name, token);
    }

    protected long create(String url, Map<String, ?> body, String token) {
        Map<?, ?> created = client.post()
            .uri(url)
            .headers(h -> h.setBearerAuth(token))
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Map.class).returnResult().getResponseBody();

        return ((Number) created.get("id")).longValue();
    }

    protected SetupData createSetupData(String token) {
        String n = String.valueOf(System.nanoTime());

        long typeId = create("/organization-types", Map.of("name", "Producer" + n), token);
        long producerId = create("/organizations", Map.of("name", "Producer " + n, "organizationNumber", "P" + n, "organizationTypeId", typeId), token);
        long productId = create("/products", Map.of("name", "Product " + n, "barcodeNumber", "B" + n, "cost", new BigDecimal("9.99"), "producerId", producerId), token);
        long packageId = create("/packages", Map.of("productId", productId, "productsAmount", 12, "volume", 0.05, "weight", 3.5), token);
        long palletId = create("/pallets", Map.of("name", "EUR", "color", "Blue", "length", 1.2, "width", 0.8, "height", 0.144, "weight", 25), token);
        long workshopId = create("/workshops", Map.of("name", "Workshop " + n, "standard", 1.5), token);
        long stationId = create("/work-stations", Map.of("stationNumber", "WS" + n, "controlNumber", "1", "type", "Active", "workshopId", workshopId), token);
        long gateId = create("/gates", Map.of("symbol", "G" + n), token);

        return new SetupData(productId, packageId, palletId, workshopId, stationId, gateId);
    }

    protected long statusId(String name, String type, String token) {
        List<Map<String, Object>> items = client.get()
            .uri("/statuses")
            .headers(h -> h.setBearerAuth(token))
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
            .returnResult()
            .getResponseBody();

        return items.stream()
            .filter(item -> name.equals(item.get("name")) && type.equals(item.get("type")))
            .map(item -> ((Number) item.get("id")).longValue())
            .findFirst()
            .orElseThrow(() -> new AssertionError("No status '" + name + "' of type '" + type + "' at /statuses"));
    }
}
