package com.infy.stg.quarq.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.infy.stg.quarq.TestUtil;
import org.junit.jupiter.api.Test;


public class EmployeeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.id = 1L;
        Employee employee2 = new Employee();
        employee2.id = employee1.id;
        assertThat(employee1).isEqualTo(employee2);
        employee2.id = 2L;
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.id = null;
        assertThat(employee1).isNotEqualTo(employee2);
    }
}
