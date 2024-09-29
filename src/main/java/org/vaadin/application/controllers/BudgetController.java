package org.vaadin.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.application.model.Budget;
import org.vaadin.application.service.BudgetService;

import java.util.List;

/**
 * Rest controller for managing budget-related operations.
 * This controller provides endpoints to get budgets by user ID, add a new budget, 
 * and delete an existing budget.
 */
@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    /**
     * Retrieves a list of budgets associated with a specific user ID.
     *
     * @param userId the ID of the user whose budgets are to be retrieved
     * @return a list of budgets associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<Budget> getBudgetsByUserId(@PathVariable Long userId) {
        return budgetService.getBudgetsByUserId(userId);
    }

    /**
     * Adds a new budget.
     *
     * @param budget the budget object to be added
     * @return the newly added budget object
     */
    @PostMapping("/add")
    public Budget addBudget(@RequestBody Budget budget) {
        return budgetService.addBudget(budget);
    }

    /**
     * Deletes a budget by its ID.
     *
     * @param id the ID of the budget to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}
