package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Expense;
import org.vaadin.example.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing expense-related operations.
 * This class interacts with the {@link ExpenseRepository} to perform CRUD operations on {@link Expense} entities.
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    /**
     * Retrieves a list of expenses associated with a specific user ID.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of expenses associated with the specified user ID
     */
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    /**
     * Adds a new expense to the repository.
     *
     * @param expense the expense object to be added
     * @return the newly added expense object
     */
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    /**
     * Finds an expense by its ID.
     *
     * @param id the ID of the expense to find
     * @return the expense object if found, or null if not found
     */
    public Expense findExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an expense by its ID.
     *
     * @param id the ID of the expense to be deleted
     */
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    /**
     * Calculates the total expenses for the current month for a specific user.
     *
     * @param userId the ID of the user whose total expenses are to be calculated
     * @return the total amount of expenses for the user in the current month
     */
    public BigDecimal getTotalExpensesForCurrentMonth(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
       
        return expenseRepository.findTotalExpensesForPeriod(userId, startOfMonth, endOfMonth);
    }

    /**
     * Retrieves a list of expenses associated with a specific budget ID.
     *
     * @param budgetId the ID of the budget whose expenses are to be retrieved
     * @return a list of expenses associated with the specified budget ID
     */
    public List<Expense> getExpensesByBudget(Long budgetId) {
        return expenseRepository.findByBudgetId(budgetId);
    }
}
