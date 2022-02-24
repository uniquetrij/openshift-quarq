package com.infy.stg.quarq.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.infy.stg.quarq.service.EmbeddingService;
import com.infy.stg.quarq.web.rest.errors.BadRequestAlertException;
import com.infy.stg.quarq.web.util.HeaderUtil;
import com.infy.stg.quarq.web.util.ResponseUtil;
import com.infy.stg.quarq.service.dto.EmbeddingDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.infy.stg.quarq.service.Paged;
import com.infy.stg.quarq.web.rest.vm.PageRequestVM;
import com.infy.stg.quarq.web.rest.vm.SortRequestVM;
import com.infy.stg.quarq.web.util.PaginationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.infy.stg.quarq.domain.Embedding}.
 */
@Path("/api/embeddings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class EmbeddingResource {

    private final Logger log = LoggerFactory.getLogger(EmbeddingResource.class);

    private static final String ENTITY_NAME = "embedding";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    EmbeddingService embeddingService;
    /**
     * {@code POST  /embeddings} : Create a new embedding.
     *
     * @param embeddingDTO the embeddingDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new embeddingDTO, or with status {@code 400 (Bad Request)} if the embedding has already an ID.
     */
    @POST
    public Response createEmbedding(EmbeddingDTO embeddingDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Embedding : {}", embeddingDTO);
        if (embeddingDTO.id != null) {
            throw new BadRequestAlertException("A new embedding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = embeddingService.persistOrUpdate(embeddingDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /embeddings} : Updates an existing embedding.
     *
     * @param embeddingDTO the embeddingDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated embeddingDTO,
     * or with status {@code 400 (Bad Request)} if the embeddingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the embeddingDTO couldn't be updated.
     */
    @PUT
    public Response updateEmbedding(EmbeddingDTO embeddingDTO) {
        log.debug("REST request to update Embedding : {}", embeddingDTO);
        if (embeddingDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = embeddingService.persistOrUpdate(embeddingDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, embeddingDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /embeddings/:id} : delete the "id" embedding.
     *
     * @param id the id of the embeddingDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEmbedding(@PathParam("id") Long id) {
        log.debug("REST request to delete Embedding : {}", id);
        embeddingService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /embeddings} : get all the embeddings.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of embeddings in body.
     */
    @GET
    public Response getAllEmbeddings(@BeanParam PageRequestVM pageRequest, @BeanParam SortRequestVM sortRequest, @Context UriInfo uriInfo, @QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get a page of Embeddings");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<EmbeddingDTO> result;
        if (eagerload) {
            result = embeddingService.findAllWithEagerRelationships(page);
        } else {
            result = embeddingService.findAll(page);
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }


    /**
     * {@code GET  /embeddings/:id} : get the "id" embedding.
     *
     * @param id the id of the embeddingDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the embeddingDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getEmbedding(@PathParam("id") Long id) {
        log.debug("REST request to get Embedding : {}", id);
        Optional<EmbeddingDTO> embeddingDTO = embeddingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(embeddingDTO);
    }
}
