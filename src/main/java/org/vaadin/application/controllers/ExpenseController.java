package org.vaadin.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.application.model.Expense;
import org.vaadin.application.service.ExpenseService;

import java.util.List;

/**
 * Rest controller for managing expense-related operations.
 * This controller provides endpoints to get expenses by user ID,
 * add a new expense, and delete an existing expense.
 */
@RestController
@RequestMapping("/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    /**
     * Retrieves a list of expenses associated with a specific user ID.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of expenses associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<Expense> getExpensesByUserId(@PathVariable Long userId) {
        return expenseService.getExpensesByUserId(userId);
    }

    /**
     * Adds a new expense.
     *
     * @param expense the expense object to be added
     * @return the newly added expense object
     */
    @PostMapping("/add")
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    /**
     * Deletes an expense by its ID.
     *
     * @param id the ID of the expense to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
