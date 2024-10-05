package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
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
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * The InvoiceView class provides the user interface for managing invoices
 * within the application.
 * Users can add, update, view, and delete invoices.
 * and allows users to fill out invoice details like recipient and amount payable.
 *
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It includes a
 * {@link com.vaadin.flow.component.grid.Grid}
 * to display the list of invoices,
 * {@link com.vaadin.flow.component.textfield.TextField} for user input, and
 * {@link com.vaadin.flow.component.button.Button} for form actions.
 * </p>
 *
 * <p>
 * The {@code @Route} annotation maps this view to the "invoice" URL path and
 * associates it with the {@link org.vaadin.application.MainLayout}.
 * </p>
 *
 * <p>
 * This class interacts with the following services:
 * {@link org.vaadin.application.service.InvoiceService}
 * for managing invoice data,
 * {@link org.vaadin.application.service.SessionService} for managing
 * session-related data, and
 * {@link org.vaadin.application.service.UserService} for retrieving user
 * information.
 * </p>
 * 
 * @see org.vaadin.application.service.ExpenseService
 * @see org.vaadin.application.service.BudgetService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "invoice", layout = MainLayout.class)
public class InvoiceView extends VerticalLayout {

    private InvoiceService invoiceService;
    private SessionService sessionService;
    private UserService userService;
    private Grid<Invoice> invoiceGrid = new Grid<>(Invoice.class);
    private Invoice selectedInvoice = null; // To track the selected invoice
    private UUID selectedInvoiceId = null;

    /**
     * Constructor that initializes the InvoiceView and its components.
     * 
     * @param invoiceService  Service to manage invoice data.
     * @param sessionService  Service to manage session and user information.
     * @param userService     Service to manage user data.
     */
    @Autowired
    public InvoiceView(InvoiceService invoiceService, SessionService sessionService, UserService userService) {
        this.invoiceService = invoiceService;
        this.sessionService = sessionService;
        this.userService = userService;

        H1 logo = new H1("Invoices");

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
                newInvoice.setInvoiceNumber("1");
                newInvoice.setRecipientName(name);
                newInvoice.setAmount(amount);
                newInvoice.setIssueDate(java.sql.Date.valueOf(issue));
                newInvoice.setDueDate(java.sql.Date.valueOf(due));
                newInvoice.setDescription(desc);
                newInvoice.setStatus(stat);
                newInvoice.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

                invoiceService.addInvoice(newInvoice);
                Notification.show("Invoice saved for: " + name + ", Amount: " + amount);
            } else {
                // Update existing invoice
                selectedInvoice.setRecipientName(name);
                selectedInvoice.setAmount(amount);
                selectedInvoice.setIssueDate(java.sql.Date.valueOf(issue));
                selectedInvoice.setDueDate(java.sql.Date.valueOf(due));
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

        // Create a button to delete the selected invoice
        Button deleteButton = new Button("Delete", event -> {
            if (selectedInvoice != null) {
                invoiceService.deleteInvoice(selectedInvoice.getId());
                Notification.show("Invoice deleted for: " + selectedInvoice.getRecipientName());
                loadInvoices(); // Reload invoices after deleting
                selectedInvoice = null;
            } else {
                Notification.show("Please select an invoice to delete.");
            }
        });

        // Create a form layout and add fields to it
        FormLayout formLayout = new FormLayout();
        formLayout.add(recipientName, amountPayable, issueDate, dueDate, description, status, saveButton, deleteButton);

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
                issueDate.setValue(selectedInvoice.getIssueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                dueDate.setValue(selectedInvoice.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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
        add(logo, formLayout, invoiceGrid);
    }

    /**
     * Loads the invoices of the currently logged-in user and displays them in the grid.
     */
    private void loadInvoices() {
        Long userId = sessionService.getLoggedInUserId();
        List<Invoice> invoices = invoiceService.getInvoicesByUserId(userId); // Assuming user ID 1 for now
        invoiceGrid.setItems(invoices); // Load invoices into the grid
    }
}
