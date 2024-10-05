package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity representing an invoice.
 * An invoice has a recipient name, an amount payable, and additional details like issue date, etc.
 */
@Entity
@Table(name = "invoice")
public class Invoice {

    /**
     * The unique identifier for the invoice.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The unique number of the invoice.
     * Cannot be null.
     */
    @NotNull
    private String invoiceNumber;

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
    private double amount;

    /**
     * The issue date of the invoice.
     * Cannot be null.
     */
    @NotNull
    private LocalDate issueDate;

    /**
     * The due date of the invoice.
     * Cannot be null.
     */
    @NotNull
    private LocalDate dueDate;

    /**
     * The description or purpose of the invoice.
     */
    private String description;

    /**
     * The status of the invoice (e.g., "Pending", "Paid", "Overdue").
     */
    @NotNull
    private String status;

    /**
     * The user associated with the asset.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the user associated with the asset.
     *
     * @return the user associated with the asset
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the asset.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
