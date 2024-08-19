package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.repository.ExpenseCategoryRepository;

import java.util.List;

/**
 * Service class for managing expense category-related operations.
 * This class interacts with the {@link ExpenseCategoryRepository} to perform CRUD operations on {@link ExpenseCategory} entities.
 */
@Service
public class ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    /**
     * Retrieves a list of expense categories associated with a specific user ID.
     *
     * @param userId the ID of the user whose expense categories are to be retrieved
     * @return a list of expense categories associated with the specified user ID
     */
    public List<ExpenseCategory> getExpenseCategoriesByUserId(Long userId) {
        return expenseCategoryRepository.findByUserId(userId);
    }

    /**
     * Adds a new expense category to the repository.
     *
     * @param expenseCategory the expense category object to be added
     * @return the newly added expense category object
     */
    public ExpenseCategory addExpenseCategory(ExpenseCategory expenseCategory) {
        return expenseCategoryRepository.save(expenseCategory);
    }

    /**
     * Finds an expense category by its ID.
     *
     * @param id the ID of the expense category to find
     * @return the expense category object if found, or null if not found
     */
    public ExpenseCategory findExpenseCategoryById(Long id) {
        return expenseCategoryRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an expense category by its ID.
     *
     * @param id the ID of the expense category to be deleted
     */
    public void deleteExpenseCategory(Long id) {
        expenseCategoryRepository.deleteById(id);
    }

    /**
     * Retrieves a list of expense categories associated with a specific user ID.
     * (Alias for {@link #getExpenseCategoriesByUserId(Long)} method.)
     *
     * @param userId the ID of the user whose expense categories are to be retrieved
     * @return a list of expense categories associated with the specified user ID
     */
    public List<ExpenseCategory> getCategoriesByUserId(Long userId) {
        return expenseCategoryRepository.findByUserId(userId);
    }
}
