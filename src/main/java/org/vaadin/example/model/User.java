package org.vaadin.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Entity representing a user.
 * A user has a unique name and password, and is associated with multiple budgets, expenses, 
 * expense categories, financial goals, and incomes.
 */
@Entity
@Table(name = "user")
public class User {

    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique name of the user.
     * Cannot be null.
     */
    @NotNull
    @Column(unique = true)
    private String name;

    /**
     * The password for the user.
     * Cannot be null.
     */
    @NotNull
    private String password;

    /**
     * The set of budgets associated with the user.
     * Mapped by the 'user' field in the Budget entity.
     * Cascading operations are applied to all related budgets.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Budget> budgets;

    /**
     * The set of expenses associated with the user.
     * Mapped by the 'user' field in the Expense entity.
     * Cascading operations are applied to all related expenses.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Expense> expenses;

    /**
     * The set of expense categories associated with the user.
     * Mapped by the 'user' field in the ExpenseCategory entity.
     * Cascading operations are applied to all related expense categories.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ExpenseCategory> expenseCategories;

    /**
     * The set of financial goals associated with the user.
     * Mapped by the 'user' field in the FinancialGoal entity.
     * Cascading operations are applied to all related financial goals.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FinancialGoal> financialGoals;

    /**
     * The set of incomes associated with the user.
     * Mapped by the 'user' field in the Income entity.
     * Cascading operations are applied to all related incomes.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Income> incomes;

    // Getters and Setters

    /**
     * Gets the unique identifier for the user.
     *
     * @return the ID of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the unique name of the user.
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the unique name of the user.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the password for the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the set of budgets associated with the user.
     *
     * @return the set of budgets
     */
    public Set<Budget> getBudgets() {
        return budgets;
    }

    /**
     * Sets the set of budgets associated with the user.
     *
     * @param budgets the set of budgets to set
     */
    public void setBudgets(Set<Budget> budgets) {
        this.budgets = budgets;
    }

    /**
     * Gets the set of expenses associated with the user.
     *
     * @return the set of expenses
     */
    public Set<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets the set of expenses associated with the user.
     *
     * @param expenses the set of expenses to set
     */
    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Gets the set of expense categories associated with the user.
     *
     * @return the set of expense categories
     */
    public Set<ExpenseCategory> getExpenseCategories() {
        return expenseCategories;
    }

    /**
     * Sets the set of expense categories associated with the user.
     *
     * @param expenseCategories the set of expense categories to set
     */
    public void setExpenseCategories(Set<ExpenseCategory> expenseCategories) {
        this.expenseCategories = expenseCategories;
    }

    /**
     * Gets the set of financial goals associated with the user.
     *
     * @return the set of financial goals
     */
    public Set<FinancialGoal> getFinancialGoals() {
        return financialGoals;
    }

    /**
     * Sets the set of financial goals associated with the user.
     *
     * @param financialGoals the set of financial goals to set
     */
    public void setFinancialGoals(Set<FinancialGoal> financialGoals) {
        this.financialGoals = financialGoals;
    }

    /**
     * Gets the set of incomes associated with the user.
     *
     * @return the set of incomes
     */
    public Set<Income> getIncomes() {
        return incomes;
    }

    /**
     * Sets the set of incomes associated with the user.
     *
     * @param incomes the set of incomes to set
     */
    public void setIncomes(Set<Income> incomes) {
        this.incomes = incomes;
    }
}
