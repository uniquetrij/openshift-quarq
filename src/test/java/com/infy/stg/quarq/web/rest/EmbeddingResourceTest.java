package com.infy.stg.quarq.web.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import com.infy.stg.quarq.TestUtil;
import com.infy.stg.quarq.service.dto.EmbeddingDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import liquibase.Liquibase;
import io.quarkus.liquibase.LiquibaseFactory;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import java.util.List;
import java.nio.ByteBuffer;
    import java.util.ArrayList;

@QuarkusTest
public class EmbeddingResourceTest {

    private static final TypeRef<EmbeddingDTO> ENTITY_TYPE = new TypeRef<>() {
    };

    private static final TypeRef<List<EmbeddingDTO>> LIST_OF_ENTITY_TYPE = new TypeRef<>() {
    };

    private static final byte[] DEFAULT_EMBEDDING = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_EMBEDDING = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_EMBEDDING_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_EMBEDDING_CONTENT_TYPE = "image/png";



    String adminToken;

    EmbeddingDTO embeddingDTO;

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
    public static EmbeddingDTO createEntity() {
        var embeddingDTO = new EmbeddingDTO();
        embeddingDTO.embedding = DEFAULT_EMBEDDING;
        return embeddingDTO;
    }

    @BeforeEach
    public void initTest() {
        embeddingDTO = createEntity();
    }

    @Test
    public void createEmbedding() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Embedding
        embeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Validate the Embedding in the database
        var embeddingDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(embeddingDTOList).hasSize(databaseSizeBeforeCreate + 1);
        var testEmbeddingDTO = embeddingDTOList.stream().filter(it -> embeddingDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmbeddingDTO.embedding).isEqualTo(DEFAULT_EMBEDDING);
    }

    @Test
    public void createEmbeddingWithExistingId() {
        var databaseSizeBeforeCreate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Create the Embedding with an existing ID
        embeddingDTO.id = 1L;

        // An entity with an existing ID cannot be created, so this API call must fail
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Embedding in the database
        var embeddingDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(embeddingDTOList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void updateEmbedding() {
        // Initialize the database
        embeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Get the embedding
        var updatedEmbeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings/{id}", embeddingDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().body().as(ENTITY_TYPE);

        // Update the embedding
        updatedEmbeddingDTO.embedding = UPDATED_EMBEDDING;

        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(updatedEmbeddingDTO)
            .when()
            .put("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode());

        // Validate the Embedding in the database
        var embeddingDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(embeddingDTOList).hasSize(databaseSizeBeforeUpdate);
        var testEmbeddingDTO = embeddingDTOList.stream().filter(it -> updatedEmbeddingDTO.id.equals(it.id)).findFirst().get();
        assertThat(testEmbeddingDTO.embedding).isEqualTo(UPDATED_EMBEDDING);
    }

    @Test
    public void updateNonExistingEmbedding() {
        var databaseSizeBeforeUpdate = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
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
            .body(embeddingDTO)
            .when()
            .put("/api/embeddings")
            .then()
            .statusCode(BAD_REQUEST.getStatusCode());

        // Validate the Embedding in the database
        var embeddingDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(embeddingDTOList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteEmbedding() {
        // Initialize the database
        embeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var databaseSizeBeforeDelete = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE)
            .size();

        // Delete the embedding
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .delete("/api/embeddings/{id}", embeddingDTO.id)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

        // Validate the database contains one less item
        var embeddingDTOList = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .extract().as(LIST_OF_ENTITY_TYPE);

        assertThat(embeddingDTOList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void getAllEmbeddings() {
        // Initialize the database
        embeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        // Get all the embeddingList
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings?sort=id,desc")
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", hasItem(embeddingDTO.id.intValue()))
            .body("embedding", hasItem(DEFAULT_EMBEDDING.toString()));
    }

    @Test
    public void getEmbedding() {
        // Initialize the database
        embeddingDTO = given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(embeddingDTO)
            .when()
            .post("/api/embeddings")
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract().as(ENTITY_TYPE);

        var response = // Get the embedding
            given()
                .auth()
                .preemptive()
                .oauth2(adminToken)
                .accept(APPLICATION_JSON)
                .when()
                .get("/api/embeddings/{id}", embeddingDTO.id)
                .then()
                .statusCode(OK.getStatusCode())
                .contentType(APPLICATION_JSON)
                .extract().as(ENTITY_TYPE);

        // Get the embedding
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings/{id}", embeddingDTO.id)
            .then()
            .statusCode(OK.getStatusCode())
            .contentType(APPLICATION_JSON)
            .body("id", is(embeddingDTO.id.intValue()))
            
                .body("embedding", is(DEFAULT_EMBEDDING.toString()));
    }

    @Test
    public void getNonExistingEmbedding() {
        // Get the embedding
        given()
            .auth()
            .preemptive()
            .oauth2(adminToken)
            .accept(APPLICATION_JSON)
            .when()
            .get("/api/embeddings/{id}", Long.MAX_VALUE)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }
}
