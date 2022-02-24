package com.infy.stg.quarq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.stg.quarq.TestUtil;
import org.junit.jupiter.api.Test;


public class FileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(File.class);
        File file1 = new File();
        file1.id = 1L;
        File file2 = new File();
        file2.id = file1.id;
        assertThat(file1).isEqualTo(file2);
        file2.id = 2L;
        assertThat(file1).isNotEqualTo(file2);
        file1.id = null;
        assertThat(file1).isNotEqualTo(file2);
    }
}
