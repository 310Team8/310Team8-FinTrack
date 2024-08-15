package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;

@Route(value = "budgets", layout = MainLayout.class)
@PageTitle("Manage Budgets")
public class BudgetView extends VerticalLayout {

  private final BudgetService budgetService;
  private final Grid<Budget> grid = new Grid<>(Budget.class);
  private final TextField nameField = new TextField("Budget Name");
  private final NumberField amountField = new NumberField("Budget Amount");
  private final Button saveButton = new Button("Save");

  @Autowired
  public BudgetView(BudgetService budgetService) {
    this.budgetService = budgetService;
    setUpForm();
    setUpGrid();
    loadData();
  }

  private void setUpForm() {
    saveButton.addClickListener(e -> saveBudget());
    add(nameField, amountField, saveButton);
  }

  private void setUpGrid() {
    grid.setColumns("id", "name", "amount");
    grid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
    add(grid);
  }

  private void loadData() {
    grid.setItems(budgetService.getAllBudgets());
  }

  private void saveBudget() {
    Budget budget = new Budget();
    budget.setName(nameField.getValue());

  
    BigDecimal amount = BigDecimal.valueOf(amountField.getValue());
    budget.setAmount(amount);

    budgetService.saveBudget(budget);
    loadData();
    Notification.show("Budget saved");
  }

  private void populateForm(Budget budget) {
    if (budget != null) {
      nameField.setValue(budget.getName());

      amountField.setValue(budget.getAmount().doubleValue());
    }
  }
}
