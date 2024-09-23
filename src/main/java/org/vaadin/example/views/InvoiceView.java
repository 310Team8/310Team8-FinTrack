package org.vaadin.example.views;

import org.vaadin.example.MainLayout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route(value = "invoice", layout = MainLayout.class)
public class InvoiceView extends VerticalLayout {

    // In-memory list to store invoices
    private List<Invoice> invoices = new ArrayList<>();
    private Grid<Invoice> invoiceGrid = new Grid<>(Invoice.class);

    public InvoiceView() {
        // Create form fields
        TextField recipientName = new TextField("Recipient Name");
        NumberField amountPayable = new NumberField("Amount Payable");

        // Create a button to save the invoice
        Button saveButton = new Button("Save", event -> {
            String name = recipientName.getValue();
            Double amount = amountPayable.getValue();

            // Check if fields are filled
            if (name.isEmpty() || amount == null) {
                Notification.show("Please fill in all fields.");
                return;
            }

            // Save the invoice to the list
            Invoice newInvoice = new Invoice(name, amount);
            invoices.add(newInvoice);
            invoiceGrid.setItems(invoices); // Refresh the grid with new data

            // Clear form fields after saving
            recipientName.clear();
            amountPayable.clear();

            // Show a confirmation notification
            Notification.show("Invoice saved for: " + name + ", Amount: " + amount);
        });

        // Create a form layout and add fields to it
        FormLayout formLayout = new FormLayout();
        formLayout.add(recipientName, amountPayable, saveButton);

        // Configure the invoice grid
        invoiceGrid.setColumns("recipientName", "amount");
        invoiceGrid.getColumnByKey("recipientName").setHeader("Recipient Name");
        invoiceGrid.getColumnByKey("amount").setHeader("Amount Payable");

        // Add the form layout and the grid to the main layout
        add(formLayout, invoiceGrid);
    }

    // Inner class to represent an Invoice
    public static class Invoice {
        private String recipientName;
        private Double amount;

        public Invoice(String recipientName, Double amount) {
            this.recipientName = recipientName;
            this.amount = amount;
        }

        public String getRecipientName() {
            return recipientName;
        }

        public void setRecipientName(String recipientName) {
            this.recipientName = recipientName;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
