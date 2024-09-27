package org.vaadin.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.application.model.Income;
import org.vaadin.application.service.IncomeService;

import java.util.List;

/**
 * Rest controller for managing income-related operations.
 * This controller provides endpoints to get incomes by user ID, 
 * add a new income, and delete an existing income.
 */
@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    /**
     * Retrieves a list of incomes associated with a specific user ID.
     *
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a list of incomes associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<Income> getIncomesByUserId(@PathVariable Long userId) {
        return incomeService.getIncomesByUserId(userId);
    }

    /**
     * Adds a new income.
     *
     * @param income the income object to be added
     * @return the newly added income object
     */
    @PostMapping("/add")
    public Income addIncome(@RequestBody Income income) {
        return incomeService.addIncome(income);
    }

    /**
     * Deletes an income by its ID.
     *
     * @param id the ID of the income to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}
