package com.infy.stg.quarq.service.mapper;


import com.infy.stg.quarq.domain.*;
import com.infy.stg.quarq.service.dto.EmbeddingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Embedding} and its DTO {@link EmbeddingDTO}.
 */
@Mapper(componentModel = "cdi", uses = {EmployeeMapper.class})
public interface EmbeddingMapper extends EntityMapper<EmbeddingDTO, Embedding> {



    default Embedding fromId(Long id) {
        if (id == null) {
            return null;
        }
        Embedding embedding = new Embedding();
        embedding.id = id;
        return embedding;
    }
}
