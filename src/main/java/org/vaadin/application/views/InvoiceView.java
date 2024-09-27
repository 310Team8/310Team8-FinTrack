package org.vaadin.application.views;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Invoice;
import org.vaadin.application.service.InvoiceService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Route(value = "invoice", layout = MainLayout.class)
public class InvoiceView extends VerticalLayout {

    private InvoiceService invoiceService;
    private SessionService sessionService;
    private UserService userService;
    private Grid<Invoice> invoiceGrid = new Grid<>(Invoice.class);

    @Autowired
    public InvoiceView(InvoiceService invoiceService, SessionService sessionService, UserService userService) {
        this.invoiceService = invoiceService;
        this.sessionService = sessionService;
        this.userService = userService;

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

        loadInvoices();

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

            // Save the invoice using the service
            Invoice newInvoice = new Invoice();
            newInvoice.setRecipientName(name);
            newInvoice.setAmount(amount);
            newInvoice.setInvoiceNumber(number);
            newInvoice.setIssueDate(issue);
            newInvoice.setDueDate(due);
            newInvoice.setDescription(desc);
            newInvoice.setStatus(stat);
            newInvoice.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
            invoiceService.addInvoice(newInvoice);
            loadInvoices(); // Reload invoices after saving

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

        // Load existing invoices when the view is created
        loadInvoices();

        // Add the form layout and the grid to the main layout
        add(formLayout, invoiceGrid);
    }

    private void loadInvoices() {
        Long userId = sessionService.getLoggedInUserId();
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId); // Assuming user ID 1 for now
        invoiceGrid.setItems(invoices); // Load invoices into the grid
    }
}
