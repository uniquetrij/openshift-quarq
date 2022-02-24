package com.infy.stg.quarq.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.infy.stg.quarq.TestUtil;
import com.infy.stg.quarq.service.dto.FileDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import io.quarkus.liquibase.LiquibaseFactory;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;
    import java.util.ArrayList;

@QuarkusTest
public class FileResourceTest {

    private static final TypeRef<FileDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<FileDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final String DEFAULT_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_URI = "AAAAAAAAAA";
    private static final String UPDATED_URI = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENCRYPTION = false;
    private static final Boolean UPDATED_ENCRYPTION = true;

    private static final EncryptionAlgorithm DEFAULT_ENCRYPTION_ALGORITHM = SHA256;
    private static final EncryptionAlgorithm UPDATED_ENCRYPTION_ALGORITHM = SHA512;



    String adminToken;

    FileDTO fileDTO;

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
    public static FileDTO createEntity() {
        var fileDTO = new FileDTO();
        fileDTO.identifier = DEFAULT_IDENTIFIER;
        fileDTO.location = DEFAULT_LOCATION;
        fileDTO.uri = DEFAULT_URI;
        fileDTO.encryption = DEFAULT_ENCRYPTION;
        fileDTO.encryptionAlgorithm = DEFAULT_ENCRYPTION_ALGORITHM;
        return fileDTO;
    }

    @BeforeEach
    public void initTest() {
        fileDTO = createEntity();
    }

    @Test
    public void createFile() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the File
        fileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the File in the database
        var fileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(fileDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testFileDTO = fileDTOList.stream().filter(it -> fileDTO.id.equals(it.id)).findFirst().get();
        assertThat(testFileDTO.identifier).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testFileDTO.location).isEqualTo(DEFAULT_LOCATION);
        assertThat(testFileDTO.uri).isEqualTo(DEFAULT_URI);
        assertThat(testFileDTO.encryption).isEqualTo(DEFAULT_ENCRYPTION);
        assertThat(testFileDTO.encryptionAlgorithm).isEqualTo(DEFAULT_ENCRYPTION_ALGORITHM);
    }

    @Test
    public void createFileWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the File with an existing ID
        fileDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the File in the database
        var fileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(fileDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void updateFile() {
        // Initialize the database
        fileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the file
        var updatedFileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files/{id}", fileDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the file
        updatedFileDTO.identifier = UPDATED_IDENTIFIER;
        updatedFileDTO.location = UPDATED_LOCATION;
        updatedFileDTO.uri = UPDATED_URI;
        updatedFileDTO.encryption = UPDATED_ENCRYPTION;
        updatedFileDTO.encryptionAlgorithm = UPDATED_ENCRYPTION_ALGORITHM;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedFileDTO)
            .when()
            .put("/api/files")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the File in the database
        var fileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(fileDTOList).hasSize(databaseSizeBeforeUpdate);
        var testFileDTO = fileDTOList.stream().filter(it -> updatedFileDTO.id.equals(it.id)).findFirst().get();
        assertThat(testFileDTO.identifier).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testFileDTO.location).isEqualTo(UPDATED_LOCATION);
        assertThat(testFileDTO.uri).isEqualTo(UPDATED_URI);
        assertThat(testFileDTO.encryption).isEqualTo(UPDATED_ENCRYPTION);
        assertThat(testFileDTO.encryptionAlgorithm).isEqualTo(UPDATED_ENCRYPTION_ALGORITHM);
    }

    @Test
    public void updateNonExistingFile() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
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
            .body(fileDTO)
            .when()
            .put("/api/files")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the File in the database
        var fileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(fileDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteFile() {
        // Initialize the database
        fileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the file
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/files/{id}", fileDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var fileDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(fileDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllFiles() {
        // Initialize the database
        fileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the fileList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(fileDTO.id.intValue()))
            .body("identifier", hasItem(DEFAULT_IDENTIFIER))            .body("location", hasItem(DEFAULT_LOCATION))            .body("uri", hasItem(DEFAULT_URI))            .body("encryption", hasItem(DEFAULT_ENCRYPTION.booleanValue()))            .body("encryptionAlgorithm", hasItem(DEFAULT_ENCRYPTION_ALGORITHM.toString()));
    }

    @Test
    public void getFile() {
        // Initialize the database
        fileDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(fileDTO)
            .when()
            .post("/api/files")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the file
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/files/{id}", fileDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the file
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files/{id}", fileDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(fileDTO.id.intValue()))
            
                .body("identifier", is(DEFAULT_IDENTIFIER))
                .body("location", is(DEFAULT_LOCATION))
                .body("uri", is(DEFAULT_URI))
                .body("encryption", is(DEFAULT_ENCRYPTION.booleanValue()))
                .body("encryptionAlgorithm", is(DEFAULT_ENCRYPTION_ALGORITHM.toString()));
    }

    @Test
    public void getNonExistingFile() {
        // Get the file
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/files/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
