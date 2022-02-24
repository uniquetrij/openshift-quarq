package com.infy.stg.quarq.service.impl;

import com.infy.stg.quarq.service.EmbeddingService;
import io.quarkus.panache.common.Page;
import com.infy.stg.quarq.service.Paged;
import com.infy.stg.quarq.domain.Embedding;
import com.infy.stg.quarq.service.dto.EmbeddingDTO;
import com.infy.stg.quarq.service.mapper.EmbeddingMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EmbeddingServiceImpl implements EmbeddingService {

    private final Logger log = LoggerFactory.getLogger(EmbeddingServiceImpl.class);

    @Inject
    EmbeddingMapper embeddingMapper;

    @Override
    @Transactional
    public EmbeddingDTO persistOrUpdate(EmbeddingDTO embeddingDTO) {
        log.debug("Request to save Embedding : {}", embeddingDTO);
        var embedding = embeddingMapper.toEntity(embeddingDTO);
        embedding = Embedding.persistOrUpdate(embedding);
        return embeddingMapper.toDto(embedding);
    }

    /**
     * Delete the Embedding by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Embedding : {}", id);
        Embedding.findByIdOptional(id).ifPresent(embedding -> {
            embedding.delete();
        });
    }

    /**
     * Get one embedding by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<EmbeddingDTO> findOne(Long id) {
        log.debug("Request to get Embedding : {}", id);
        return Embedding.findOneWithEagerRelationships(id)
            .map(embedding -> embeddingMapper.toDto((Embedding) embedding)); 
    }

    /**
     * Get all the embeddings.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<EmbeddingDTO> findAll(Page page) {
        log.debug("Request to get all Embeddings");
        return new Paged<>(Embedding.findAll().page(page))
            .map(embedding -> embeddingMapper.toDto((Embedding) embedding));
    }


    /**
     * Get all the embeddings with eager load of many-to-many relationships.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<EmbeddingDTO> findAllWithEagerRelationships(Page page) {
        var embeddings = Embedding.findAllWithEagerRelationships().page(page).list();
        var totalCount = Embedding.findAll().count();
        var pageCount = Embedding.findAll().page(page).pageCount();
        return new Paged<>(page.index, page.size, totalCount, pageCount, embeddings)
            .map(embedding -> embeddingMapper.toDto((Embedding) embedding));
    }


}
