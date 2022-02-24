package com.infy.stg.quarq.service;

import com.infy.stg.quarq.service.dto.FileDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.stg.quarq.domain.File}.
 */
public interface FileService {

    /**
     * Save a file.
     *
     * @param fileDTO the entity to save.
     * @return the persisted entity.
     */
    FileDTO persistOrUpdate(FileDTO fileDTO);

    /**
     * Delete the "id" fileDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the files.
     * @return the list of entities.
     */
    public  List<FileDTO> findAll();

    /**
     * Get the "id" fileDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileDTO> findOne(Long id);


    /**
     * Get all the files with eager load of many-to-many relationships.
     * @return the list of entities.
    */
    public  List<FileDTO> findAllWithEagerRelationships();


}
