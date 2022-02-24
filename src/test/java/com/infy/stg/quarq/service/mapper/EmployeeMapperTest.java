package com.infy.stg.quarq.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeMapperTest {

    private EmployeeMapper employeeMapper;

    @BeforeEach
    public void setUp() {
        employeeMapper = new EmployeeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(employeeMapper.fromId(id).id).isEqualTo(id);
        assertThat(employeeMapper.fromId(null)).isNull();
    }
}
