package com.infy.stg.quarq.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.infy.stg.quarq.TestUtil;

public class PersonDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonDTO.class);
        PersonDTO personDTO1 = new PersonDTO();
        personDTO1.id = 1L;
        PersonDTO personDTO2 = new PersonDTO();
        assertThat(personDTO1).isNotEqualTo(personDTO2);
        personDTO2.id = personDTO1.id;
        assertThat(personDTO1).isEqualTo(personDTO2);
        personDTO2.id = 2L;
        assertThat(personDTO1).isNotEqualTo(personDTO2);
        personDTO1.id = null;
        assertThat(personDTO1).isNotEqualTo(personDTO2);
    }
}
