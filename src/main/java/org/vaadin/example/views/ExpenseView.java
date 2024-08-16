package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Expense;
import org.vaadin.example.service.ExpenseService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "expense", layout = MainLayout.class)
public class ExpenseView extends VerticalLayout {

    private final Grid<Expense> grid = new Grid<>(Expense.class);
    private final TextField descriptionField = new TextField("Description");
    private final TextField amountField = new TextField("Amount");
    private final TextField dateField = new TextField("Date");

    private final ExpenseService expenseService;
    private final SessionService sessionService;
    private final UserService userService;

    public ExpenseView(ExpenseService expenseService, SessionService sessionService, UserService userService) {
        this.expenseService = expenseService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();

        Button addButton = new Button("Add Expense", event -> addExpense());
        Button deleteButton = new Button("Delete Expense", event -> deleteExpense());

        HorizontalLayout formLayout = new HorizontalLayout(descriptionField, amountField, dateField, addButton, deleteButton);
        add(formLayout, grid);

        listExpenses();
    }

    private void configureGrid() {
        grid.setColumns("description", "amount", "date", "category.name");
        grid.getColumnByKey("category.name").setHeader("Category");
    }

    private void listExpenses() {
        Long userId = sessionService.getLoggedInUserId();
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        grid.setItems(expenses);
    }

    private void addExpense() {
        String description = descriptionField.getValue();
        String amountText = amountField.getValue();
        BigDecimal amount = new BigDecimal(amountText);
        String date = dateField.getValue();

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setDate(java.sql.Date.valueOf(date));
        expense.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

        expenseService.addExpense(expense);
        Notification.show("Expense added successfully");
        listExpenses();
        clearForm();
    }

    private void deleteExpense() {
        Expense selectedExpense = grid.asSingleSelect().getValue();
        if (selectedExpense != null) {
            expenseService.deleteExpense(selectedExpense.getId());
            Notification.show("Expense deleted successfully");
            listExpenses();
        } else {
            Notification.show("Please select an expense to delete");
        }
    }

    private void clearForm() {
        descriptionField.clear();
        amountField.clear();
        dateField.clear();
    }
}
