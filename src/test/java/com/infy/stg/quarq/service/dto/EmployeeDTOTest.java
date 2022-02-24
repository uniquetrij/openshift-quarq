package com.infy.stg.quarq.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.infy.stg.quarq.TestUtil;

public class EmployeeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDTO.class);
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.id = 1L;
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO2.id = employeeDTO1.id;
        assertThat(employeeDTO1).isEqualTo(employeeDTO2);
        employeeDTO2.id = 2L;
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO1.id = null;
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
    }
}
