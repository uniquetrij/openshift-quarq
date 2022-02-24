package com.infy.stg.quarq.service.impl;

import com.infy.stg.quarq.service.EmployeeService;
import io.quarkus.panache.common.Page;
import com.infy.stg.quarq.service.Paged;
import com.infy.stg.quarq.domain.Employee;
import com.infy.stg.quarq.service.dto.EmployeeDTO;
import com.infy.stg.quarq.service.mapper.EmployeeMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Inject
    EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeDTO persistOrUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        var employee = employeeMapper.toEntity(employeeDTO);
        employee = Employee.persistOrUpdate(employee);
        return employeeMapper.toDto(employee);
    }

    /**
     * Delete the Employee by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        Employee.findByIdOptional(id).ifPresent(employee -> {
            employee.delete();
        });
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return Employee.findByIdOptional(id)
            .map(employee -> employeeMapper.toDto((Employee) employee)); 
    }

    /**
     * Get all the employees.
     * @param page the pagination information.
     * @return the list of entities.
     */
    @Override
    public Paged<EmployeeDTO> findAll(Page page) {
        log.debug("Request to get all Employees");
        return new Paged<>(Employee.findAll().page(page))
            .map(employee -> employeeMapper.toDto((Employee) employee));
    }



}
