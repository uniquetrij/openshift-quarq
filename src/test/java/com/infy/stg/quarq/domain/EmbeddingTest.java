package com.infy.stg.quarq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.stg.quarq.TestUtil;
import org.junit.jupiter.api.Test;


public class EmbeddingTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Embedding.class);
        Embedding embedding1 = new Embedding();
        embedding1.id = 1L;
        Embedding embedding2 = new Embedding();
        embedding2.id = embedding1.id;
        assertThat(embedding1).isEqualTo(embedding2);
        embedding2.id = 2L;
        assertThat(embedding1).isNotEqualTo(embedding2);
        embedding1.id = null;
        assertThat(embedding1).isNotEqualTo(embedding2);
    }
}
