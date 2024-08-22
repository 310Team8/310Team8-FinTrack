package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "budget", layout = MainLayout.class)
public class BudgetView extends VerticalLayout {

    private final Grid<Budget> grid = new Grid<>(Budget.class);
    private final TextField nameField = new TextField("Budget Name");
    private final TextField amountField = new TextField("Amount");

    private final BudgetService budgetService;
    private final SessionService sessionService;
    private final UserService userService;

    // Constructor Injection
    public BudgetView(BudgetService budgetService, SessionService sessionService, UserService userService) {
        this.budgetService = budgetService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();
        
        Button addButton = new Button("Add Budget", event -> addBudget());
        Button deleteButton = new Button("Delete Budget", event -> deleteBudget());

        HorizontalLayout formLayout = new HorizontalLayout(nameField, amountField, addButton, deleteButton);
        add(formLayout, grid);

        listBudgets();
    }

    private void configureGrid() {
        // Exclude id and user fields from being displayed
        grid.setColumns("name", "amount");
        grid.getColumnByKey("amount").setHeader("Amount ($)"); // Customize header
    }

    private void listBudgets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        grid.setItems(budgets);
    }

    private void addBudget() {
        try {
            String name = nameField.getValue();
            String amountText = amountField.getValue();
            BigDecimal amount = new BigDecimal(amountText);

            Budget budget = new Budget();
            budget.setName(name);
            budget.setAmount(amount);
            budget.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

            budgetService.addBudget(budget);
            Notification.show("Budget added successfully", 3000, Notification.Position.TOP_CENTER);
            
            clearForm();
            listBudgets();
        } catch (NumberFormatException e) {
            Notification.show("Please enter a valid amount", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void deleteBudget() {
        Budget selectedBudget = grid.asSingleSelect().getValue();
        if (selectedBudget != null) {
            budgetService.deleteBudget(selectedBudget.getId());
            Notification.show("Budget deleted successfully", 3000, Notification.Position.TOP_CENTER);
            listBudgets();
        } else {
            Notification.show("Please select a budget to delete", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        nameField.clear();
        amountField.clear();
    }
}
