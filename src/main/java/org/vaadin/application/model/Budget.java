package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * Entity representing a budget.
 * A budget is associated with a user and has a name and amount.
 */
@Entity
@Table(name = "budget")
public class Budget {
    /**
     * The unique identifier for the budget.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the budget.
     * Cannot be null.
     */
    @NotNull
    private String name;

    /**
     * The amount allocated for the budget.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    /**
     * The user associated with the budget.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The icon associated with the budget.
     */
    private String icon;

    /**
     * The current amount spent in the budget.
     */
    private BigDecimal currentAmount = BigDecimal.ZERO;



    // Getters and Setters

    /**
     * Gets the unique identifier for the budget.
     *
     * @return the ID of the budget
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the budget.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the budget.
     *
     * @return the name of the budget
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the budget.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the amount allocated for the budget.
     *
     * @return the amount of the budget
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount allocated for the budget.
     *
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the user associated with the budget.
     *
     * @return the user associated with the budget
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the budget.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the icon associated with the budget.
     *
     * @return the icon associated with the budget
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon associated with the budget.
     *
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Gets the current amount spent in the budget.
     *
     * @return the current amount spent in the budget
     */
    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }
    

    /**
     * Sets the current amount spent in the budget.
     *
     * @param currentAmount the current amount to set
     */
    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }
}