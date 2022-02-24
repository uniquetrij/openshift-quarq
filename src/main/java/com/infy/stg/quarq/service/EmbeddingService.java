package com.infy.stg.quarq.service;

import com.infy.stg.quarq.service.dto.EmbeddingDTO;
import io.quarkus.panache.common.Page;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.stg.quarq.domain.Embedding}.
 */
public interface EmbeddingService {

    /**
     * Save a embedding.
     *
     * @param embeddingDTO the entity to save.
     * @return the persisted entity.
     */
    EmbeddingDTO persistOrUpdate(EmbeddingDTO embeddingDTO);

    /**
     * Delete the "id" embeddingDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the embeddings.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<EmbeddingDTO> findAll(Page page);

    /**
     * Get the "id" embeddingDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmbeddingDTO> findOne(Long id);


    /**
     * Get all the embeddings with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
    */
    public Paged<EmbeddingDTO> findAllWithEagerRelationships(Page page);


}
