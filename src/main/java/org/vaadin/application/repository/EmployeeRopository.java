package org.vaadin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.Employee;

/**
 * repository interface for managing {@link Employee} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface EmployeeRopository extends JpaRepository<Employee, Long> {

    /**
     * Finds a list of Employees associated with a specific user ID.
     *
     * @param userId the ID of the user whose Employees are to be retrieved
     * @return a list of Employees associated with the specified user ID
     */
    List<Employee> findByUserId(Long userId);
    
}
