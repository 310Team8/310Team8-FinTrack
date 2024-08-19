package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;

import java.util.List;

/**
 * Rest controller for managing expense category-related operations.
 * This controller provides endpoints to get expense categories by user ID, 
 * add a new expense category, and delete an existing expense category.
 */
@RestController
@RequestMapping("/expense-category")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    /**
     * Retrieves a list of expense categories associated with a specific user ID.
     *
     * @param userId the ID of the user whose expense categories are to be retrieved
     * @return a list of expense categories associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<ExpenseCategory> getExpenseCategoriesByUserId(@PathVariable Long userId) {
        return expenseCategoryService.getExpenseCategoriesByUserId(userId);
    }

    /**
     * Adds a new expense category.
     *
     * @param expenseCategory the expense category object to be added
     * @return the newly added expense category object
     */
    @PostMapping("/add")
    public ExpenseCategory addExpenseCategory(@RequestBody ExpenseCategory expenseCategory) {
        return expenseCategoryService.addExpenseCategory(expenseCategory);
    }

    /**
     * Deletes an expense category by its ID.
     *
     * @param id the ID of the expense category to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteExpenseCategory(@PathVariable Long id) {
        expenseCategoryService.deleteExpenseCategory(id);
    }
}
