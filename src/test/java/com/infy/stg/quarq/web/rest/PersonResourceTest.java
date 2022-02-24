package com.infy.stg.quarq.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.infy.stg.quarq.TestUtil;
import com.infy.stg.quarq.service.dto.PersonDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import io.quarkus.liquibase.LiquibaseFactory;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;
    import java.time.Instant;
import java.time.temporal.ChronoUnit;

@QuarkusTest
public class PersonResourceTest {

    private static final TypeRef<PersonDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<PersonDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH = Instant.ofEpochSecond(0L).truncatedTo(ChronoUnit.SECONDS);
    private static final Instant UPDATED_BIRTH = Instant.now().truncatedTo(ChronoUnit.SECONDS);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";



    String adminToken;

    PersonDTO personDTO;

    @Inject
    LiquibaseFactory liquibaseFactory;

    @BeforeAll
    static void jsonMapper() {
        RestAssured.config =
            RestAssured.config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(TestUtil.jsonbObjectMapper()));
    }

    @BeforeEach
    public void authenticateAdmin() {
        this.adminToken = TestUtil.getAdminToken();
    }

    @BeforeEach
    public void databaseFixture() {
        try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
            liquibase.dropAll();
            liquibase.validate();
            liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonDTO createEntity() {
        var personDTO = new PersonDTO();
        personDTO.name = DEFAULT_NAME;
        personDTO.birth = DEFAULT_BIRTH;
        personDTO.status = DEFAULT_STATUS;
        personDTO.country = DEFAULT_COUNTRY;
        personDTO.city = DEFAULT_CITY;
        return personDTO;
    }

    @BeforeEach
    public void initTest() {
        personDTO = createEntity();
    }

    @Test
    public void createPerson() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Person
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testPersonDTO = personDTOList.stream().filter(it -> personDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPersonDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonDTO.birth).isEqualTo(DEFAULT_BIRTH);
        assertThat(testPersonDTO.status).isEqualTo(DEFAULT_STATUS);
        assertThat(testPersonDTO.country).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPersonDTO.city).isEqualTo(DEFAULT_CITY);
    }

    @Test
    public void createPersonWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Person with an existing ID
        personDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void updatePerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the person
        var updatedPersonDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the person
        updatedPersonDTO.name = UPDATED_NAME;
        updatedPersonDTO.birth = UPDATED_BIRTH;
        updatedPersonDTO.status = UPDATED_STATUS;
        updatedPersonDTO.country = UPDATED_COUNTRY;
        updatedPersonDTO.city = UPDATED_CITY;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedPersonDTO)
            .when()
            .put("/api/people")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeUpdate);
        var testPersonDTO = personDTOList.stream().filter(it -> updatedPersonDTO.id.equals(it.id)).findFirst().get();
        assertThat(testPersonDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testPersonDTO.birth).isEqualTo(UPDATED_BIRTH);
        assertThat(testPersonDTO.status).isEqualTo(UPDATED_STATUS);
        assertThat(testPersonDTO.country).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPersonDTO.city).isEqualTo(UPDATED_CITY);
    }

    @Test
    public void updateNonExistingPerson() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .put("/api/people")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Person in the database
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var personDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(personDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllPeople() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the personList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(personDTO.id.intValue()))
            .body("name", hasItem(DEFAULT_NAME))            .body("birth", hasItem(TestUtil.formatDateTime(DEFAULT_BIRTH)))            .body("status", hasItem(DEFAULT_STATUS))            .body("country", hasItem(DEFAULT_COUNTRY))            .body("city", hasItem(DEFAULT_CITY));
    }

    @Test
    public void getPerson() {
        // Initialize the database
        personDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(personDTO)
            .when()
            .post("/api/people")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the person
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/people/{id}", personDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", personDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(personDTO.id.intValue()))
            
                .body("name", is(DEFAULT_NAME))
                .body("birth", is(TestUtil.formatDateTime(DEFAULT_BIRTH)))
                .body("status", is(DEFAULT_STATUS))
                .body("country", is(DEFAULT_COUNTRY))
                .body("city", is(DEFAULT_CITY));
    }

    @Test
    public void getNonExistingPerson() {
        // Get the person
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/people/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
