package com.infy.stg.quarq.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddingMapperTest {

    private EmbeddingMapper embeddingMapper;

    @BeforeEach
    public void setUp() {
        embeddingMapper = new EmbeddingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(embeddingMapper.fromId(id).id).isEqualTo(id);
        assertThat(embeddingMapper.fromId(null)).isNull();
    }
}
