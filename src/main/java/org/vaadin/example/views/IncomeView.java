package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.Income;
import org.vaadin.example.service.IncomeService;
import org.vaadin.example.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Route(value = "incomes", layout = MainLayout.class)
@PageTitle("Manage Income")
public class IncomeView extends VerticalLayout {

    private final IncomeService incomeService;
    private final Grid<Income> grid = new Grid<>(Income.class);
    private final TextField sourceField = new TextField("Source");
    private final NumberField amountField = new NumberField("Amount");
    private final DatePicker dateField = new DatePicker("Date");
    private final Button saveButton = new Button("Save");

    @Autowired
    public IncomeView(IncomeService incomeService) {
        this.incomeService = incomeService;
        setUpForm();
        setUpGrid();
        loadData();
    }

    private void setUpForm() {
        saveButton.addClickListener(e -> saveIncome());
        add(sourceField, amountField, dateField, saveButton);
    }

    private void setUpGrid() {
        grid.setColumns("id", "source", "amount", "date");
        grid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
        add(grid);
    }

    private void loadData() {
        grid.setItems(incomeService.getAllIncomes());
    }

    private void saveIncome() {
        Income income = new Income();
        income.setSource(sourceField.getValue());

        BigDecimal amount = BigDecimal.valueOf(amountField.getValue());
        income.setAmount(amount);

        income.setDate(dateField.getValue());
        incomeService.saveIncome(income);
        loadData();
        Notification.show("Income saved");
    }

    private void populateForm(Income income) {
        if (income != null) {
            sourceField.setValue(income.getSource());

            amountField.setValue(income.getAmount().doubleValue());

            dateField.setValue(income.getDate());
        }
    }
}
