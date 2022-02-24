package com.infy.stg.quarq.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.infy.stg.quarq.service.FileService;
import com.infy.stg.quarq.web.rest.errors.BadRequestAlertException;
import com.infy.stg.quarq.web.util.HeaderUtil;
import com.infy.stg.quarq.web.util.ResponseUtil;
import com.infy.stg.quarq.service.dto.FileDTO;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.infy.stg.quarq.domain.File}.
 */
@Path("/api/files")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    FileService fileService;
    /**
     * {@code POST  /files} : Create a new file.
     *
     * @param fileDTO the fileDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new fileDTO, or with status {@code 400 (Bad Request)} if the file has already an ID.
     */
    @POST
    public Response createFile(FileDTO fileDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save File : {}", fileDTO);
        if (fileDTO.id != null) {
            throw new BadRequestAlertException("A new file cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = fileService.persistOrUpdate(fileDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /files} : Updates an existing file.
     *
     * @param fileDTO the fileDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated fileDTO,
     * or with status {@code 400 (Bad Request)} if the fileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fileDTO couldn't be updated.
     */
    @PUT
    public Response updateFile(FileDTO fileDTO) {
        log.debug("REST request to update File : {}", fileDTO);
        if (fileDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = fileService.persistOrUpdate(fileDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fileDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /files/:id} : delete the "id" file.
     *
     * @param id the id of the fileDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam("id") Long id) {
        log.debug("REST request to delete File : {}", id);
        fileService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /files} : get all the files.
     *     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of files in body.
     */
    @GET
    public List<FileDTO> getAllFiles(@QueryParam(value = "eagerload") boolean eagerload) {
        log.debug("REST request to get all Files");
        return fileService.findAll();
    }


    /**
     * {@code GET  /files/:id} : get the "id" file.
     *
     * @param id the id of the fileDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the fileDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getFile(@PathParam("id") Long id) {
        log.debug("REST request to get File : {}", id);
        Optional<FileDTO> fileDTO = fileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fileDTO);
    }
}
