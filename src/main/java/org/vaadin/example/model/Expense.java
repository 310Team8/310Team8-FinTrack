package org.vaadin.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity representing an expense.
 * An expense is associated with a user and may belong to an expense category. 
 * It has a description, amount, and date.
 */
@Entity
@Table(name = "expense")
public class Expense {
    /**
     * The unique identifier for the expense.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A description of the expense.
     * Cannot be null.
     */
    @NotNull
    private String description;

    /**
     * The amount of the expense.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    /**
     * The date when the expense was incurred.
     * Cannot be null.
     */
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    /**
     * The category to which the expense belongs.
     * This is optional.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private ExpenseCategory category;

    /**
     * The user who incurred the expense.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and Setters

    /**
     * Gets the unique identifier for the expense.
     *
     * @return the ID of the expense
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the expense.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the description of the expense.
     *
     * @return the description of the expense
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the expense.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the amount of the expense.
     *
     * @return the amount of the expense
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the date when the expense was incurred.
     *
     * @return the date of the expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date when the expense was incurred.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the category to which the expense belongs.
     *
     * @return the category of the expense
     */
    public ExpenseCategory getCategory() {
        return category;
    }

    /**
     * Sets the category to which the expense belongs.
     *
     * @param category the category to set
     */
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    /**
     * Gets the user who incurred the expense.
     *
     * @return the user associated with the expense
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who incurred the expense.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
