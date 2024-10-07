package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * Entity representing an employee.
 * An employee is associated with a user and contains information about employees,
 * such as their name, role, and salary.
 */
@Entity
@Table(name = "employee")
public class Employee {

    /**
     * The unique identifier for the employee.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The salary of the employee.
     * Cannot be null.
     * Must be greater than 0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salary;

    /**
     * The name of the employee.
     * Cannot be null.
     */
    @NotNull
    private String employeeName;

    /**
     * The role of the employee.
     * Cannot be null.
     */
    @NotNull
    private String employeeRole;

    /**
     * The user associated with the employee.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
