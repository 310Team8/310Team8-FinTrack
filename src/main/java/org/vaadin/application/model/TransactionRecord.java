package org.vaadin.application.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRecord {
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String details;
    private String type;
    
    /**
     * Constructor for transaction record.
     * 
     * @param transactionDate date of transaction
     * @param amount absolute value of transaction amount
     * @param details further transaction details
     * @param type income or expense
     */
    public TransactionRecord(LocalDate transactionDate, BigDecimal amount, String details, String type){
        this.amount = amount;
        this.details = details;
        this.transactionDate = transactionDate;
        this.type = type;
    }

    /**
     * Gets the transaction amount.
     * 
     * @return the transaction amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Gets the transaction date.
     * 
     * @return the transaction date
     */
    public LocalDate getTransactionDate(){
        return transactionDate;
    }

    /**
     * Gets the transaction details.
     * 
     * @return the transaction details
     */
    public String getDetails(){
        return details;
    }

    /**
     * Gets the transaction type.
     * 
     * @return the transaction type (Income or Expense)
     */
    public String getType(){
        return type;
    }

    /**
     * Sets the transaction details.
     * 
     * @param details transaction details to be set
     */
    public void setDetails(String details){
        this.details = details;
    }
}
