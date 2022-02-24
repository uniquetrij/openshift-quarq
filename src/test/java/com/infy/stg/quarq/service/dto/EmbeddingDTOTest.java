package com.infy.stg.quarq.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.infy.stg.quarq.TestUtil;

public class EmbeddingDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmbeddingDTO.class);
        EmbeddingDTO embeddingDTO1 = new EmbeddingDTO();
        embeddingDTO1.id = 1L;
        EmbeddingDTO embeddingDTO2 = new EmbeddingDTO();
        assertThat(embeddingDTO1).isNotEqualTo(embeddingDTO2);
        embeddingDTO2.id = embeddingDTO1.id;
        assertThat(embeddingDTO1).isEqualTo(embeddingDTO2);
        embeddingDTO2.id = 2L;
        assertThat(embeddingDTO1).isNotEqualTo(embeddingDTO2);
        embeddingDTO1.id = null;
        assertThat(embeddingDTO1).isNotEqualTo(embeddingDTO2);
    }
}
