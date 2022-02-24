package com.infy.stg.quarq.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.infy.stg.quarq.TestUtil;
import com.infy.stg.quarq.service.dto.EmployeeDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import io.quarkus.liquibase.LiquibaseFactory;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;
    
@QuarkusTest
public class EmployeeResourceTest {

    private static final TypeRef<EmployeeDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<EmployeeDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENCRYPTION_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ENCRYPTION_KEY = "BBBBBBBBBB";



    String adminToken;

    EmployeeDTO employeeDTO;

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
    public static EmployeeDTO createEntity() {
        var employeeDTO = new EmployeeDTO();
        employeeDTO.identifier = DEFAULT_IDENTIFIER;
        employeeDTO.name = DEFAULT_NAME;
        employeeDTO.encryptionKey = DEFAULT_ENCRYPTION_KEY;
        return employeeDTO;
    }

    @BeforeEach
    public void initTest() {
        employeeDTO = createEntity();
    }

    @Test
    public void createEmployee() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testEmployeeDTO = employeeDTOList.stream().filter(it -> employeeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeDTO.identifier).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testEmployeeDTO.name).isEqualTo(DEFAULT_NAME);
        assertThat(testEmployeeDTO.encryptionKey).isEqualTo(DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void createEmployeeWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Employee with an existing ID
        employeeDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void updateEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the employee
        var updatedEmployeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the employee
        updatedEmployeeDTO.identifier = UPDATED_IDENTIFIER;
        updatedEmployeeDTO.name = UPDATED_NAME;
        updatedEmployeeDTO.encryptionKey = UPDATED_ENCRYPTION_KEY;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEmployeeDTO)
            .when()
            .put("/api/employees")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeUpdate);
        var testEmployeeDTO = employeeDTOList.stream().filter(it -> updatedEmployeeDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmployeeDTO.identifier).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testEmployeeDTO.name).isEqualTo(UPDATED_NAME);
        assertThat(testEmployeeDTO.encryptionKey).isEqualTo(UPDATED_ENCRYPTION_KEY);
    }

    @Test
    public void updateNonExistingEmployee() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
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
            .body(employeeDTO)
            .when()
            .put("/api/employees")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Employee in the database
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var employeeDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(employeeDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEmployees() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the employeeList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(employeeDTO.id.intValue()))
            .body("identifier", hasItem(DEFAULT_IDENTIFIER))            .body("name", hasItem(DEFAULT_NAME))            .body("encryptionKey", hasItem(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void getEmployee() {
        // Initialize the database
        employeeDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(employeeDTO)
            .when()
            .post("/api/employees")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the employee
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/employees/{id}", employeeDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", employeeDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(employeeDTO.id.intValue()))
            
                .body("identifier", is(DEFAULT_IDENTIFIER))
                .body("name", is(DEFAULT_NAME))
                .body("encryptionKey", is(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void getNonExistingEmployee() {
        // Get the employee
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/employees/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
