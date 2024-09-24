package org.vaadin.example.views;

import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Invoice;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
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
        TextField invoiceNumber = new TextField("Invoice Number");
        DatePicker issueDate = new DatePicker("Issue Date");
        DatePicker dueDate = new DatePicker("Due Date");
        TextArea description = new TextArea("Description");
        ComboBox<String> status = new ComboBox<>("Status");
        status.setItems("Pending", "Paid", "Overdue");
        status.setValue("Pending");

        // Create a button to save the invoice
        Button saveButton = new Button("Save", event -> {
            String name = recipientName.getValue();
            Double amount = amountPayable.getValue();
            String number = invoiceNumber.getValue();
            LocalDate issue = issueDate.getValue();
            LocalDate due = dueDate.getValue();
            String desc = description.getValue();
            String stat = status.getValue();

            // Check if fields are filled
            if (name.isEmpty() || amount == null || number.isEmpty() || issue == null || due == null) {
                Notification.show("Please fill in all required fields.");
                return;
            }

            // Save the invoice to the list
            Invoice newInvoice = new Invoice(number, name, amount, issue, due, desc, stat);
            invoices.add(newInvoice);
            invoiceGrid.setItems(invoices); // Refresh the grid with new data

            // Clear form fields after saving
            recipientName.clear();
            amountPayable.clear();
            invoiceNumber.clear();
            issueDate.clear();
            dueDate.clear();
            description.clear();
            status.setValue("Pending");

            // Show a confirmation notification
            Notification.show("Invoice saved for: " + name + ", Amount: " + amount);
        });

        // Create a form layout and add fields to it
        FormLayout formLayout = new FormLayout();
        formLayout.add(recipientName, amountPayable, invoiceNumber, issueDate, dueDate, description, status, saveButton);

        // Configure the invoice grid
        invoiceGrid.setColumns("invoiceNumber", "recipientName", "amount", "issueDate", "dueDate", "status");
        invoiceGrid.getColumnByKey("invoiceNumber").setHeader("Invoice Number");
        invoiceGrid.getColumnByKey("recipientName").setHeader("Recipient Name");
        invoiceGrid.getColumnByKey("amount").setHeader("Amount Payable");
        invoiceGrid.getColumnByKey("issueDate").setHeader("Issue Date");
        invoiceGrid.getColumnByKey("dueDate").setHeader("Due Date");
        invoiceGrid.getColumnByKey("status").setHeader("Status");

        // Add the form layout and the grid to the main layout
        add(formLayout, invoiceGrid);
    }
}
