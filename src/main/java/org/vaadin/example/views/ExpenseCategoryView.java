package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;
import org.vaadin.example.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "categories", layout = MainLayout.class)
@PageTitle("Manage Categories")
public class ExpenseCategoryView extends VerticalLayout {

    private final ExpenseCategoryService categoryService;
    private final Grid<ExpenseCategory> grid = new Grid<>(ExpenseCategory.class);
    private final TextField nameField = new TextField("Category Name");
    private final Button saveButton = new Button("Save");

    @Autowired
    public ExpenseCategoryView(ExpenseCategoryService categoryService) {
        this.categoryService = categoryService;
        setUpForm();
        setUpGrid();
        loadData();
    }

    private void setUpForm() {
        saveButton.addClickListener(e -> saveCategory());
        add(nameField, saveButton);
    }

    private void setUpGrid() {
        grid.setColumns("id", "name");
        grid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
        add(grid);
    }

    private void loadData() {
        grid.setItems(categoryService.getAllCategories());
    }

    private void saveCategory() {
        ExpenseCategory category = new ExpenseCategory();
        category.setName(nameField.getValue());
        categoryService.saveCategory(category);
        loadData();
        Notification.show("Category saved");
    }

    private void populateForm(ExpenseCategory category) {
        if (category != null) {
            nameField.setValue(category.getName());
        }
    }
}
