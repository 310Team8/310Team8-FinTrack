package org.vaadin.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * Entity representing an expense category.
 * An expense category is associated with a user and can contain multiple expenses.
 */
@Entity
@Table(name = "expense_category")
public class ExpenseCategory {
   /**
     * The unique identifier for the expense category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the expense category.
     * Cannot be null.
     */
    @NotNull
    private String name;

    /**
     * The user associated with the expense category.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The set of expenses associated with this expense category.
     * Mapped by the 'category' field in the Expense entity.
     * Cascading operations are applied to all related expenses.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Expense> expenses;

    // Getters and Setters

    /**
     * Gets the unique identifier for the expense category.
     *
     * @return the ID of the expense category
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the expense category.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the expense category.
     *
     * @return the name of the expense category
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the expense category.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the user associated with the expense category.
     *
     * @return the user associated with the expense category
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the expense category.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the set of expenses associated with this expense category.
     *
     * @return the set of expenses
     */
    public Set<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets the set of expenses associated with this expense category.
     *
     * @param expenses the set of expenses to set
     */
    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }
}
