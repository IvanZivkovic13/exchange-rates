package com.europoint.exchangerates;

import com.europoint.exchangerates.exchangeratesapi.dtos.requests.client.CreateNewClientReqDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExchangeRatesTest {
    @Value("${exchange-rates.service.base-url}")
    private String serviceBaseUrl;
    private static final String MASTER_AGENT_ID = "a28577a5-5924-4d7b-9ea0-53117831e08c";
    private static final String USER_ID = "7aade182-434a-43b8-9dee-fab93f080ac6";
    private static Map<String, String> headers = new HashMap<>();
    static String clientId;
    static String clientName;
    static String updatedClientName;
    static String parentClientId;
    static String contributorId;
    static String exchangeRateListId ;
    static String exchangeRateListTypeId;
    static String referenceCurrencyCode = "RSD";
    static String customKey;
    static String templateId;
    static String currencyCode;
    static String contributorRefCode;
    static  String contributorRefNumber;
    static String contributorName;
    static String contributorType;
    static String contributorAccessType;
    static String country = "SRB";
    static String exchangeRateListVersionId;
    static String accessKey;

    @BeforeAll
    static void setup(){
        headers.put("MasterAgentId", MASTER_AGENT_ID);
        clientId = "79422cd7-c808-4631-a273-45c1a108e998";
        clientName= "Tenfore" ;
        contributorId = UUID.randomUUID().toString();
        parentClientId = "1944e4d5-b4c4-4576-aef5-1da40c10b7ef";
        contributorRefCode = "NBS-" + RandomStringUtils.randomAlphabetic(2);
        contributorRefNumber = "2023-" + RandomStringUtils.randomAlphanumeric(3);
        contributorName = "Narodna Banka Srbija";
        contributorType = "EXTERNAL";
        contributorAccessType = "PUBLIC";
        exchangeRateListTypeId = "6a65d348-b21f-4790-8ad7-2633bc4fa91c";
    }

    @BeforeEach
    void setupEntity() {

        templateId = UUID.randomUUID().toString();
        exchangeRateListVersionId = UUID.randomUUID().toString();
        updatedClientName= RandomStringUtils.randomAlphabetic(6) + "-updated";
        exchangeRateListId = String.valueOf(UUID.randomUUID());
        customKey = String.valueOf(UUID.randomUUID());
        accessKey = RandomStringUtils.randomNumeric(7);
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Create and Apply ExchangeRate List Version Template")
    class create_applyExchangeRateListVersionTemplate{

        @Test
        @Order(1)
        public void createClient() {
            headers.put("UserId", USER_ID);

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            exchangeRateListRequest.setId(exchangeRateListId);

            Permission permission = new Permission();
            permission.setType("MANAGE_CONTRIBUTOR");
            permission.setContributorId(contributorRequest.getId());
            permission.setExchangeRateListId(exchangeRateListRequest.getId());

            List<Permission> permissions = new ArrayList<>();
            permissions.add(permission);

            //ClientRequest request = new ClientRequest();
            CreateNewClientReqDto createNewClientReqDto = new CreateNewClientReqDto();
            createNewClientReqDto.setId(UUID.fromString(clientId));
            createNewClientReqDto.setName(clientName);
            createNewClientReqDto.setParentClientId(UUID.fromString(parentClientId));
            createNewClientReqDto.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(createNewClientReqDto)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(2)
        void createExchangeRateListVersionTemplate() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ExchangeRateListVersionTemplateRequest.Offset offset = new ExchangeRateListVersionTemplateRequest.Offset();
            offset.setValue(1);
            offset.setType("PERCENTAGE");

            ExchangeRateListVersionTemplateRequest.Rule rule = new ExchangeRateListVersionTemplateRequest.Rule();
            rule.setTemplateId(templateId);
            rule.setCurrencyCode("EUR");
            rule.setRefExchangeRateType("ASK");
            rule.setTargetExchangeRateType("ASK");
            rule.setOffset(offset);

            List<ExchangeRateListVersionTemplateRequest.Rule> rules = new ArrayList<>();
            rules.add(rule);

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(id);

            ExchangeRateListVersionTemplateRequest request = new ExchangeRateListVersionTemplateRequest();
            request.setClientId(clientRequest.getId());
            request.setName("Sablon za Tenfore Redovnu");
            request.setRules(rules);

            given()
                    .headers(headers)
                    .body(request)
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .when()
                    .contentType("application/json")
                    .post(serviceBaseUrl + "/exchangeRates/exchangeRateListVersionTemplate/createNewExchangeRateListVersionTemplate")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(3)
        void applyExchangeRateListVersionTemplate() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListVersionTemplate/getAllExchangeRateListVersionTemplates")
                    .then()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.modificationRules.templateId");

            ExchangeRateListVersionTemplateRequest.Rule templateRequest = new ExchangeRateListVersionTemplateRequest.Rule();
            templateRequest.setTemplateId(id);

            ExchangeRateListVersionRequest exchangeRateListVersionRequest = new ExchangeRateListVersionRequest();
            exchangeRateListVersionRequest.setExchangeRateListId(exchangeRateListVersionId);

            ApplyExchangeRateListVersionTemplate request = new ApplyExchangeRateListVersionTemplate();
            request.setTemplateId(templateRequest.getTemplateId());
            request.setExchangeRateListVersionId(exchangeRateListVersionRequest.getExchangeRateListId());

            given()
                    .body(request)
                    .log().body()
                    .when()
                    .contentType("application/json")
                    .post(serviceBaseUrl + "/exchangeRates/exchangeRateListVersionTemplate/applyExchangeRateListVersionTemplate")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Create Exchange Rate List Version")
    class createExchangeRateListVersion {

        static String lastCreatedListVersionId;
        static String lastCreatedListId;

        @Test
        @Order(1)
        void createNewClient() {
            headers.put("UserId", USER_ID);

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            //ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            //exchangeRateListRequest.setId(exchangeRateListId);

            Permission permission = new Permission();
            permission.setType("MANAGE_CONTRIBUTOR");
            permission.setContributorId(contributorRequest.getId());
            //permission.setExchangeRateListId(exchangeRateListRequest.getId());

            List<Permission> permissions = new ArrayList<>();
            //permissions.add(permission);

            ClientRequest request = new ClientRequest();
            request.setId(clientId);
            request.setName(clientName);
            request.setParentClientId(parentClientId);
            request.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(2)
        void createNewContributor() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(id);

            ContributorRequest request = new ContributorRequest();
            request.setId(contributorId);
            request.setClientId(clientRequest.getId());
            request.setType(contributorType);
            request.setAccessType(contributorAccessType);
            request.setRefCode(contributorRefCode);
            request.setName(contributorName);
            request.setCountry(country);
            request.setValidFrom(String.valueOf(Instant.now()));
            request.setValidTo(String.valueOf(Instant.now().plus(100, ChronoUnit.DAYS)));

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/contributor/createNewContributor")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(3)
        void createNewExchangeRateList() {

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            ExchangeRateListTypeRequest exchangeRateListTypeRequest = new ExchangeRateListTypeRequest();
            exchangeRateListTypeRequest.setId(exchangeRateListTypeId);

            ExchangeRateListRequest request = new ExchangeRateListRequest();
            request.setId(exchangeRateListId);
            request.setContributorId(contributorRequest.getId());
            request.setExchangeRateListTypeId(exchangeRateListTypeRequest.getId());
            request.setReferenceCurrencyCode(referenceCurrencyCode);
            request.setName("NBS Redovna");

            lastCreatedListId = given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/exchangeRateList/createNewExchangeRateList")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().jsonPath().getString("result.id");
        }

        @Test
        @Order(4)
        void createNewExchangeRateListVersion() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateList/findAllAvailableExchangeRateLists")
                    .then()
                    .statusCode(200)
                    .body("result.id[-1]", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ExchangeRateRequest rate = new ExchangeRateRequest();
            rate.setBaseCurrencyCode("RSD");
            rate.setQuoteCurrencyCode("EUR");
            rate.setType("BID");
            rate.setCountryCode("EUR");
            rate.setUnit(1);
            rate.setValue(117.2737);

            List<ExchangeRateRequest> rates = new ArrayList<>();
            rates.add(rate);

            ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            exchangeRateListRequest.setId(id);

            ExchangeRateListVersionRequest request = new ExchangeRateListVersionRequest();
            //request.setId(exchangeRateListVersionId);
            request.setExchangeRateListId(id);
            request.setValidFrom(String.valueOf(Instant.now()));
            request.setValidTo(String.valueOf(Instant.now().plus(100, ChronoUnit.DAYS)));
            request.setContributorRefNumber(contributorRefNumber);
            request.setExchangeRates(rates);

            lastCreatedListVersionId = given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/exchangeRateListVersion/createNewExchangeRateListVersion")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().jsonPath().getString("result.exchangeRates.exchangeRateListVersionId");
        }

        @Test
        @Order(5)
        void findExchangeRateListVersionById() {

            Map<String, Object> data = new HashMap<>();
            data.put("exchangeRateListVersionId", lastCreatedListVersionId);

            given()
                    .headers(headers)
                    .log().body()
                    .body(data)
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListVersion/findExchangeRateListVersionById")
                    .then()
                    .statusCode(200)
                    .log().body();
        }

        @Test
        @Order(6)
        void findAllVersionsForExchangeRateListId() {
            Map<String, Object> data = new HashMap<>();
            data.put("exchangeRateListId", lastCreatedListId);

            given()
                    .headers(headers)
                    .log().body()
                    .body(data)
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListVersion/findAllVersionsForExchangeRateListId")
                    .then()
                    .statusCode(200)
                    .log().body();
        }

        @Test
        @Order(7)
        void findActiveVersionsForExchangeRateListId() {
            Map<String, Object> data = new HashMap<>();
            data.put("exchangeRateListId", lastCreatedListId);

            given()
                    .headers(headers)
                    .log().body()
                    .body(data)
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListVersion/findActiveVersionForExchangeRateListId")
                    .then()
                    .statusCode(200)
                    .log().body();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Create ExchangeRateList")
    class createExchangeRateList {

        @Test
        @Order(1)
        void createNewClient() {
            headers.put("UserId", USER_ID);

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            //ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            //exchangeRateListRequest.setId(exchangeRateListId);

            Permission permission = new Permission();
            permission.setType("MANAGE_CONTRIBUTOR");
            permission.setContributorId(contributorRequest.getId());
            //permission.setExchangeRateListId(exchangeRateListRequest.getId());

            List<Permission> permissions = new ArrayList<>();
            //permissions.add(permission);

            ClientRequest request = new ClientRequest();
            request.setId(clientId);
            request.setName(clientName);
            request.setParentClientId(parentClientId);
            request.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(2)
        void createNewContributor() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(id);

            ContributorRequest request = new ContributorRequest();
            request.setId(contributorId);
            request.setClientId(clientRequest.getId());
            request.setType(contributorType);
            request.setAccessType(contributorAccessType);
            request.setRefCode(contributorRefCode);
            request.setName(contributorName);
            request.setCountry(country);
            request.setValidFrom(String.valueOf(Instant.now()));
            request.setValidTo(String.valueOf(Instant.now().plus(100, ChronoUnit.DAYS)));

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/contributor/createNewContributor")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(3)
        void createNewExchangeRateList() {

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            ExchangeRateListTypeRequest exchangeRateListTypeRequest = new ExchangeRateListTypeRequest();
            exchangeRateListTypeRequest.setId(exchangeRateListTypeId);

            ExchangeRateListRequest request = new ExchangeRateListRequest();
            request.setId(exchangeRateListId);
            request.setContributorId(contributorRequest.getId());
            request.setExchangeRateListTypeId(exchangeRateListTypeRequest.getId());
            request.setReferenceCurrencyCode(referenceCurrencyCode);
            request.setName("NBS Redovna");

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/exchangeRateList/createNewExchangeRateList")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Create Contributor")
    class createContributor {
        private static final String USER_ID = "7aade182-434a-43b8-9dee-fab93f080ac6";
        @Test
        @Order(1)
        void createNewClient() {
            headers.put("UserId", USER_ID);

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            //ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            //exchangeRateListRequest.setId(exchangeRateListId);

            Permission permission = new Permission();
            permission.setType("MANAGE_CONTRIBUTOR");
            permission.setContributorId(contributorRequest.getId());
            //permission.setExchangeRateListId(exchangeRateListRequest.getId());

            List<Permission> permissions = new ArrayList<>();
            //permissions.add(permission);

            ClientRequest request = new ClientRequest();
            request.setId(clientId);
            request.setName(clientName);
            request.setParentClientId(parentClientId);
            request.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }
        @Test
        @Order(2)
        void createNewContributor() {

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result.id[-1]", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(id);

            ContributorRequest request = new ContributorRequest();
            request.setId(contributorId);
            request.setClientId(clientRequest.getId());
            request.setType(contributorType);
            request.setAccessType(contributorAccessType);
            request.setRefCode(contributorRefCode);
            request.setName(contributorName);
            request.setCountry(country);
            request.setValidFrom(String.valueOf(Instant.now()));
            request.setValidTo(String.valueOf(Instant.now().plus(100, ChronoUnit.DAYS)));

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/contributor/createNewContributor")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        void findContributorById() {
            Map<String, Object> data = new HashMap<>();
            data.put("contributorId ", contributorId );

            given()
                    .headers(headers)
                    .log().headers()
                    .body(data)
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/contributor/findContributorById")
                    .then()
                    .statusCode(200)
                    .log().body();
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Create and Update ExchangeRates Client and Create Access Key")
    class createAndUpdateExchangeRatesClient {
        private static final String USER_ID = "7aade182-434a-43b8-9dee-fab93f080ac6";

        @Test
        @Order(1)
        void createNewClient() {
            headers.put("UserId", USER_ID);

            ContributorRequest contributorRequest = new ContributorRequest();
            contributorRequest.setId(contributorId);

            //ExchangeRateListRequest exchangeRateListRequest = new ExchangeRateListRequest();
            //exchangeRateListRequest.setId(exchangeRateListId);

            Permission permission = new Permission();
            permission.setType("MANAGE_CONTRIBUTOR");
            permission.setContributorId(contributorRequest.getId());
            //permission.setExchangeRateListId(exchangeRateListRequest.getId());

            List<Permission> permissions = new ArrayList<>();
            //permissions.add(permission);

            ClientRequest request = new ClientRequest();
            request.setId(clientId);
            request.setName(clientName);
            request.setParentClientId(parentClientId);
            request.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(2)
        void getIdAndCreateNewAccessKey() {
            headers.put("UserId", USER_ID);

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setId(id);

            AccessKeyRequest request = new AccessKeyRequest();
            request.setClientId(id);
            request.setValidFrom(String.valueOf(Instant.now()));
            request.setValidTo(String.valueOf(Instant.now().plus(100, ChronoUnit.DAYS)));
            request.setCustomKey(customKey);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/createNewAccessKey")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(3)
        void updateClientName() {
            headers.put("UserId", USER_ID);

            List<Permission> permissions = new ArrayList<>();

            UpdateClientRequest request = new UpdateClientRequest();
            request.setName(updatedClientName);
            request.setPermissions(permissions);

            given()
                    .headers(headers)
                    .body(request)
                    .log().headers()
                    .log().body()
                    .when()
                    .contentType(ContentType.JSON)
                    .post(serviceBaseUrl + "/exchangeRates/client/updateClient")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }

        @Test
        @Order(4)
        void findClientById() {

            headers.put("UserId", USER_ID);

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result.id[-1]", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            Map<String, Object> data = new HashMap<>();
            data.put("clientId", id);

            given()
                    .headers(headers)
                    .queryParam("clientId", id)
                    .log().headers()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findClientById")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .body("result.id", equalTo(id));
        }

        @Test
        @Order(5)
        void findClientByAccessKey() {

            given()
                    .headers(headers)
                    .log().headers()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findClientByAccessKey")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue())
                    .body("result.id", equalTo(parentClientId));
        }

        @Test
        @Order(6)
        void findAllClientAccessKey() {
            headers.put("UserId", USER_ID);

            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .body("result.id[-1]", notNullValue())
                    .extract().response();

            String id = response.jsonPath().getString("result.id[-1]");

            Map<String, Object> data = new HashMap<>();
            data.put("clientId", id);

            given()
                    .headers(headers)
                    .queryParam("clientId", id)
                    .log().headers()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClientAccessKeys")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("result", notNullValue());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("GET methods without queryParam")
    class runGetMethods {

        @Test
        @Order(1)
        void getAllExchangeRateListVersionTemplates() {
            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListVersionTemplate/getAllExchangeRateListVersionTemplates")
                    .then()
                    .statusCode(200)
                    .extract().response();
        }

        @Test
        @Order(2)
        void findAllExchangeRateListTypes() {
            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateListType/findAllExchangeRateListTypes")
                    .then()
                    .statusCode(200)
                    .extract().response();
        }

        @Test
        @Order(3)
        void findAllAvailableExchangeRateLists() {
            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/exchangeRateList/findAllAvailableExchangeRateLists")
                    .then()
                    .statusCode(200)
                    .extract().response();
        }

        @Test
        @Order(4)
        void findAllAvailableContributors() {
            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/contributor/findAllAvailableContributors")
                    .then()
                    .statusCode(200)
                    .extract().response();
        }

        @Test
        @Order(5)
        void findAllClients() {
            Response response = given()
                    .headers(headers)
                    .log().headers()
                    .log().body()
                    .when()
                    .get(serviceBaseUrl + "/exchangeRates/client/findAllClients")
                    .then()
                    .statusCode(200)
                    .extract().response();
        }
    }
}
