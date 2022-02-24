package com.infy.stg.quarq.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.infy.stg.quarq.TestUtil;

public class FileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDTO.class);
        FileDTO fileDTO1 = new FileDTO();
        fileDTO1.id = 1L;
        FileDTO fileDTO2 = new FileDTO();
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
        fileDTO2.id = fileDTO1.id;
        assertThat(fileDTO1).isEqualTo(fileDTO2);
        fileDTO2.id = 2L;
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
        fileDTO1.id = null;
        assertThat(fileDTO1).isNotEqualTo(fileDTO2);
    }
}
