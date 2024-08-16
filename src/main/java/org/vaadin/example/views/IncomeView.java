package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Income;
import org.vaadin.example.service.IncomeService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "income", layout = MainLayout.class)
public class IncomeView extends VerticalLayout {

    private final Grid<Income> grid = new Grid<>(Income.class);
    private final TextField sourceField = new TextField("Source");
    private final TextField amountField = new TextField("Amount");
    private final TextField dateField = new TextField("Date");

    private final IncomeService incomeService;
    private final SessionService sessionService;
    private final UserService userService;

    public IncomeView(IncomeService incomeService, SessionService sessionService, UserService userService) {
        this.incomeService = incomeService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();

        Button addButton = new Button("Add Income", event -> addIncome());
        Button deleteButton = new Button("Delete Income", event -> deleteIncome());

        HorizontalLayout formLayout = new HorizontalLayout(sourceField, amountField, dateField, addButton, deleteButton);
        add(formLayout, grid);

        listIncomes();
    }

    private void configureGrid() {
        grid.setColumns("source", "amount", "date");
    }

    private void listIncomes() {
        Long userId = sessionService.getLoggedInUserId();
        List<Income> incomes = incomeService.getIncomesByUserId(userId);
        grid.setItems(incomes);
    }

    private void addIncome() {
        String source = sourceField.getValue();
        String amountText = amountField.getValue();
        BigDecimal amount = new BigDecimal(amountText);
        String date = dateField.getValue();

        Income income = new Income();
        income.setSource(source);
        income.setAmount(amount);
        income.setDate(java.sql.Date.valueOf(date));
        income.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

        incomeService.addIncome(income);
        Notification.show("Income added successfully");
        listIncomes();
        clearForm();
    }

    private void deleteIncome() {
        Income selectedIncome = grid.asSingleSelect().getValue();
        if (selectedIncome != null) {
            incomeService.deleteIncome(selectedIncome.getId());
            Notification.show("Income deleted successfully");
            listIncomes();
        } else {
            Notification.show("Please select an income to delete");
        }
    }

    private void clearForm() {
        sourceField.clear();
        amountField.clear();
        dateField.clear();
    }
}
