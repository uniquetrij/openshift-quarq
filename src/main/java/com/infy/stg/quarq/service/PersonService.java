package com.infy.stg.quarq.service;

import com.infy.stg.quarq.service.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.stg.quarq.domain.Person}.
 */
public interface PersonService {

    /**
     * Save a person.
     *
     * @param personDTO the entity to save.
     * @return the persisted entity.
     */
    PersonDTO persistOrUpdate(PersonDTO personDTO);

    /**
     * Delete the "id" personDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the people.
     * @return the list of entities.
     */
    public  List<PersonDTO> findAll();

    /**
     * Get the "id" personDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonDTO> findOne(Long id);



}
