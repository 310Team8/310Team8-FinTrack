package org.vaadin.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing an invoice.
 * An invoice has a recipient name and an amount payable.
 */
@Entity
@Table(name = "invoice")
public class Invoice {
    /**
     * The unique identifier for the invoice.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the recipient for the invoice.
     * Cannot be null.
     */
    @NotNull
    private String recipientName;

    /**
     * The amount payable for the invoice.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double amount;

    /**
     * Default constructor for JPA.
     */
    public Invoice() {}

    /**
     * Constructs an Invoice with the given recipient name and amount payable.
     *
     * @param recipientName the name of the recipient
     * @param amount the amount payable
     */
    public Invoice(String recipientName, Double amount) {
        this.recipientName = recipientName;
        this.amount = amount;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the invoice.
     *
     * @return the ID of the invoice
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the invoice.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the recipient for the invoice.
     *
     * @return the name of the recipient
     */
    public String getRecipientName() {
        return recipientName;
    }

    /**
     * Sets the name of the recipient for the invoice.
     *
     * @param recipientName the name to set
     */
    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    /**
     * Gets the amount payable for the invoice.
     *
     * @return the amount payable
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount payable for the invoice.
     *
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
