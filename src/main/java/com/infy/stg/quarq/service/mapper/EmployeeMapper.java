package com.infy.stg.quarq.service.mapper;


import com.infy.stg.quarq.domain.*;
import com.infy.stg.quarq.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {


    @Mapping(target = "embeddings", ignore = true)
    @Mapping(target = "files", ignore = true)
    Employee toEntity(EmployeeDTO employeeDTO);

    default Employee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.id = id;
        return employee;
    }
}
