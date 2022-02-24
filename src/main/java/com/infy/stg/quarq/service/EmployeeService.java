package com.infy.stg.quarq.service;

import com.infy.stg.quarq.service.dto.EmployeeDTO;
import io.quarkus.panache.common.Page;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.infy.stg.quarq.domain.Employee}.
 */
public interface EmployeeService {

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    EmployeeDTO persistOrUpdate(EmployeeDTO employeeDTO);

    /**
     * Delete the "id" employeeDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the employees.
     * @param page the pagination information.
     * @return the list of entities.
     */
    public Paged<EmployeeDTO> findAll(Page page);

    /**
     * Get the "id" employeeDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EmployeeDTO> findOne(Long id);



}
