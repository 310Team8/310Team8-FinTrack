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
import java.util.UUID;

@Route(value = "invoice", layout = MainLayout.class)
public class InvoiceView extends VerticalLayout {

    private InvoiceService invoiceService;
    private SessionService sessionService;
    private UserService userService;
    private Grid<Invoice> invoiceGrid = new Grid<>(Invoice.class);
    private Invoice selectedInvoice = null; // To track the selected invoice
    private UUID selectedInvoiceId = null;

    @Autowired
    public InvoiceView(InvoiceService invoiceService, SessionService sessionService, UserService userService) {
        this.invoiceService = invoiceService;
        this.sessionService = sessionService;
        this.userService = userService;

        // Create form fields
        TextField recipientName = new TextField("Recipient Name");
        NumberField amountPayable = new NumberField("Amount Payable");
        DatePicker issueDate = new DatePicker("Issue Date");
        DatePicker dueDate = new DatePicker("Due Date");
        TextArea description = new TextArea("Description");
        ComboBox<String> status = new ComboBox<>("Status");
        status.setItems("Pending", "Paid", "Overdue");
        status.setValue("Pending");

        loadInvoices();

        // Create a button to save or update the invoice
        Button saveButton = new Button("Save", event -> {
            String name = recipientName.getValue();
            Double amount = amountPayable.getValue();
            LocalDate issue = issueDate.getValue();
            LocalDate due = dueDate.getValue();
            String desc = description.getValue();
            String stat = status.getValue();

            // Check if fields are filled
            if (name.isEmpty() || amount == null || issue == null || due == null) {
                Notification.show("Please fill in all required fields.");
                return;
            }

            if (selectedInvoice == null) {
                // Create new invoice
                Invoice newInvoice = new Invoice();
                newInvoice.setRecipientName(name);
                newInvoice.setAmount(amount);
                newInvoice.setIssueDate(issue);
                newInvoice.setDueDate(due);
                newInvoice.setDescription(desc);
                newInvoice.setStatus(stat);
                newInvoice.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

                invoiceService.addInvoice(newInvoice);
                Notification.show("Invoice saved for: " + name + ", Amount: " + amount);
            } else {
                // Update existing invoice
                selectedInvoice.setRecipientName(name);
                selectedInvoice.setAmount(amount);
                selectedInvoice.setIssueDate(issue);
                selectedInvoice.setDueDate(due);
                selectedInvoice.setDescription(desc);
                selectedInvoice.setStatus(stat);
                invoiceService.updateInvoice(selectedInvoiceId, selectedInvoice);
                Notification.show("Invoice updated for: " + name + ", Amount: " + amount);
            }

            loadInvoices(); // Reload invoices after saving or updating

            // Clear form fields and reset selectedInvoice
            recipientName.clear();
            amountPayable.clear();
            issueDate.clear();
            dueDate.clear();
            description.clear();
            status.setValue("Pending");
            selectedInvoice = null;
        });

        // Create a form layout and add fields to it
        FormLayout formLayout = new FormLayout();
        formLayout.add(recipientName, amountPayable, issueDate, dueDate, description, status, saveButton);

        // Configure the invoice grid
        invoiceGrid.setColumns( "recipientName", "amount", "issueDate", "dueDate", "status");
        invoiceGrid.getColumnByKey("recipientName").setHeader("Recipient Name");
        invoiceGrid.getColumnByKey("amount").setHeader("Amount Payable");
        invoiceGrid.getColumnByKey("issueDate").setHeader("Issue Date");
        invoiceGrid.getColumnByKey("dueDate").setHeader("Due Date");
        invoiceGrid.getColumnByKey("status").setHeader("Status");

        // Add listener for row selection
        invoiceGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedInvoice = event.getValue();
            if (selectedInvoice != null) {
                recipientName.setValue(selectedInvoice.getRecipientName());
                amountPayable.setValue(selectedInvoice.getAmount());
                issueDate.setValue(selectedInvoice.getIssueDate());
                dueDate.setValue(selectedInvoice.getDueDate());
                description.setValue(selectedInvoice.getDescription());
                status.setValue(selectedInvoice.getStatus());
                this.selectedInvoiceId = selectedInvoice.getId();
            } else {
                // Clear the form when nothing is selected
                recipientName.clear();
                amountPayable.clear();
                issueDate.clear();
                dueDate.clear();
                description.clear();
                status.setValue("Pending");
            }
        });

        // Add the form layout and the grid to the main layout
        add(formLayout, invoiceGrid);
    }

    private void loadInvoices() {
        Long userId = sessionService.getLoggedInUserId();
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId); // Assuming user ID 1 for now
        invoiceGrid.setItems(invoices); // Load invoices into the grid
    }
}
