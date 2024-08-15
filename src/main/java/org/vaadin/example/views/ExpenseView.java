package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.Expense;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;
import org.vaadin.example.service.ExpenseService;
import org.vaadin.example.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "expenses", layout = MainLayout.class)
@PageTitle("Manage Expenses")
public class ExpenseView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final ExpenseCategoryService categoryService;
    private final Grid<Expense> grid = new Grid<>(Expense.class);
    private final TextField descriptionField = new TextField("Description");
    private final NumberField amountField = new NumberField("Amount");
    private final DatePicker dateField = new DatePicker("Date");
    private final ComboBox<ExpenseCategory> categoryComboBox = new ComboBox<>("Category");
    private final Button saveButton = new Button("Save");

    @Autowired
    public ExpenseView(ExpenseService expenseService, ExpenseCategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        setUpForm();
        setUpGrid();
        loadData();
    }

    private void setUpForm() {
        List<ExpenseCategory> categories = categoryService.getAllCategories();
        categoryComboBox.setItems(categories);
        categoryComboBox.setItemLabelGenerator(ExpenseCategory::getName);

        saveButton.addClickListener(e -> saveExpense());

        add(descriptionField, amountField, dateField, categoryComboBox, saveButton);
    }

    private void setUpGrid() {
        grid.setColumns("id", "description", "amount", "date", "category.name");
        grid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
        add(grid);
    }

    private void loadData() {
        grid.setItems(expenseService.getAllExpenses());
    }

    private void saveExpense() {
    Expense expense = new Expense();
    expense.setDescription(descriptionField.getValue());

   
    BigDecimal amount = BigDecimal.valueOf(amountField.getValue());
    expense.setAmount(amount);

    expense.setDate(dateField.getValue());
    expense.setCategory(categoryComboBox.getValue());
    expenseService.saveExpense(expense);
    loadData();
    Notification.show("Expense saved");
}


private void populateForm(Expense expense) {
    if (expense != null) {
        descriptionField.setValue(expense.getDescription());
        amountField.setValue(expense.getAmount().doubleValue());

        dateField.setValue(expense.getDate());
        categoryComboBox.setValue(expense.getCategory());
    }
}

}
