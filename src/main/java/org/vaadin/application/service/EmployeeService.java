package org.vaadin.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.Employee;
import org.vaadin.application.repository.EmployeeRopository;

import java.util.List;

/**
 * Service class for managing employee-related operations. This class interacts with the {@link EmployeeRopository}
 * to perform CRUD operations on {@link Employee} entities.
 */
@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRopository employeeRopository;

    /**
     * Retrieves a list of employees associated with a specific user ID.
     *
     * @param userId the ID of the user whose employees are to be retrieved
     * @return a list of employees associated with the specified user ID
     */
    public List<Employee> getEmployeesByUserId(Long userId) {
        return employeeRopository.findByUserId(userId);
    }

    /**
     * Adds a new employee to the repository.
     *
     * @param employee the employee object to be added
     * @return the newly added employee object
     */
    public Employee addEmployee(Employee employee) {
        return employeeRopository.save(employee);
    }

    /**
     * Updates an existing employee.
     *
     * @param employeeId the ID of the employee to update
     * @param updatedEmployee the updated employee object with new details
     * @return the updated employee object, or throws an exception if not found
     */
    public Employee findEmployeeById(Long id) {
        return employeeRopository.findById(id).orElse(null);
    }

    /**
     * Updates an existing employee.
     *
     * @param employeeId the ID of the employee to update
     * @param updatedEmployee the updated employee object with new details
     * @return the updated employee object, or throws an exception if not found
     */
    public void deleteEmployee(Long id) {
        employeeRopository.deleteById(id);
    }
}
