package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity representing an income.
 * An income record is associated with a user and includes details such as the source, amount, date, and payment frequency.
 */
@Entity
@Table(name = "income")
public class Income {
    /**
     * The unique identifier for the income.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The source of the income.
     * Cannot be null.
     */
    @NotNull
    private String source;

    /**
     * The amount of the income.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    /**
     * The date when the income was received.
     * Cannot be null.
     */
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    /**
     * The user associated with the income.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * The payment frequency of the income (e.g., weekly, monthly).
     * Cannot be null.
     */
    @NotNull
    private String paymentFrequency;

    // Getters and Setters

    /**
     * Gets the unique identifier for the income.
     *
     * @return the ID of the income
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the income.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the source of the income.
     *
     * @return the source of the income
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the source of the income.
     *
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Gets the amount of the income.
     *
     * @return the amount of the income
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the income.
     *
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Gets the date when the income was received.
     *
     * @return the date of the income
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date when the income was received.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the user associated with the income.
     *
     * @return the user associated with the income
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the income.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the payment frequency of the income.
     *
     * @return the payment frequency of the income
     */
    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    /**
     * Sets the payment frequency of the income.
     *
     * @param paymentFrequency the payment frequency to set
     */
    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }
}
