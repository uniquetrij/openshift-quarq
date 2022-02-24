package com.infy.stg.quarq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.stg.quarq.TestUtil;
import org.junit.jupiter.api.Test;


public class PersonTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Person.class);
        Person person1 = new Person();
        person1.id = 1L;
        Person person2 = new Person();
        person2.id = person1.id;
        assertThat(person1).isEqualTo(person2);
        person2.id = 2L;
        assertThat(person1).isNotEqualTo(person2);
        person1.id = null;
        assertThat(person1).isNotEqualTo(person2);
    }
}
