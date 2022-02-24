package com.infy.stg.quarq.web.rest;

import static javax.ws.rs.core.UriBuilder.fromPath;

import com.infy.stg.quarq.service.PersonService;
import com.infy.stg.quarq.web.rest.errors.BadRequestAlertException;
import com.infy.stg.quarq.web.util.HeaderUtil;
import com.infy.stg.quarq.web.util.ResponseUtil;
import com.infy.stg.quarq.service.dto.PersonDTO;

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
 * REST controller for managing {@link com.infy.stg.quarq.domain.Person}.
 */
@Path("/api/people")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final String ENTITY_NAME = "person";

    @ConfigProperty(name = "application.name")
    String applicationName;


    @Inject
    PersonService personService;
    /**
     * {@code POST  /people} : Create a new person.
     *
     * @param personDTO the personDTO to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new personDTO, or with status {@code 400 (Bad Request)} if the person has already an ID.
     */
    @POST
    public Response createPerson(PersonDTO personDTO, @Context UriInfo uriInfo) {
        log.debug("REST request to save Person : {}", personDTO);
        if (personDTO.id != null) {
            throw new BadRequestAlertException("A new person cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = personService.persistOrUpdate(personDTO);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /people} : Updates an existing person.
     *
     * @param personDTO the personDTO to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated personDTO,
     * or with status {@code 400 (Bad Request)} if the personDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personDTO couldn't be updated.
     */
    @PUT
    public Response updatePerson(PersonDTO personDTO) {
        log.debug("REST request to update Person : {}", personDTO);
        if (personDTO.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = personService.persistOrUpdate(personDTO);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personDTO.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" person.
     *
     * @param id the id of the personDTO to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        log.debug("REST request to delete Person : {}", id);
        personService.delete(id);
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /people} : get all the people.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of people in body.
     */
    @GET
    public List<PersonDTO> getAllPeople() {
        log.debug("REST request to get all People");
        return personService.findAll();
    }


    /**
     * {@code GET  /people/:id} : get the "id" person.
     *
     * @param id the id of the personDTO to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the personDTO, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")

    public Response getPerson(@PathParam("id") Long id) {
        log.debug("REST request to get Person : {}", id);
        Optional<PersonDTO> personDTO = personService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personDTO);
    }
}
