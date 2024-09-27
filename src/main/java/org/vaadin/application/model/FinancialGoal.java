package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * Entity representing a financial goal.
 * A financial goal is associated with a user and has a description and a target
 * amount.
 */
@Entity
@Table(name = "financial_goal")
public class FinancialGoal {
    /**
     * The unique identifier for the financial goal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A description of the financial goal.
     * Cannot be null.
     */
    @NotNull
    private String description;

    /**
     * The target amount for achieving the financial goal.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal targetAmount;

    /**
     * The user associated with the financial goal.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal amountSaved;

    // Getters and Setters

    /**
     * Gets the unique identifier for the financial goal.
     *
     * @return the ID of the financial goal
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the financial goal.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the description of the financial goal.
     *
     * @return the description of the financial goal
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the financial goal.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the target amount for achieving the financial goal.
     *
     * @return the target amount of the financial goal
     */
    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    /**
     * Sets the target amount for achieving the financial goal.
     *
     * @param targetAmount the target amount to set
     */
    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    /**
     * Gets the user associated with the financial goal.
     *
     * @return the user associated with the financial goal
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the financial goal.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the saved amount for achieving the financial goal.
     *
     * @return the amountSaved associated with the goal.
     */

    public BigDecimal getSavedAmount() {
        return amountSaved;
    }

    /**
     * Sets the saved amount for achieving the financial goal.
     *
     * @param amountSaved the saved amount to set
     */
    public void setSavedAmount(BigDecimal amountSaved) {
        this.amountSaved = amountSaved;
    }
}
