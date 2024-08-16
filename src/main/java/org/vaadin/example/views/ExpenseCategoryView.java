package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.util.List;

@Route(value = "category", layout = MainLayout.class)
public class ExpenseCategoryView extends VerticalLayout {

    private final Grid<ExpenseCategory> grid = new Grid<>(ExpenseCategory.class);
    private final TextField nameField = new TextField("Category Name");

    private final ExpenseCategoryService expenseCategoryService;
    private final SessionService sessionService;
    private final UserService userService;

    public ExpenseCategoryView(ExpenseCategoryService expenseCategoryService, SessionService sessionService, UserService userService) {
        this.expenseCategoryService = expenseCategoryService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();
        
        Button addButton = new Button("Add Category", event -> addCategory());
        Button deleteButton = new Button("Delete Category", event -> deleteCategory());

        HorizontalLayout formLayout = new HorizontalLayout(nameField, addButton, deleteButton);
        add(formLayout, grid);

        listCategories();
    }

    private void configureGrid() {
        // Exclude id and user fields from being displayed
        grid.setColumns("name");
    }

    private void listCategories() {
        Long userId = sessionService.getLoggedInUserId();
        List<ExpenseCategory> categories = expenseCategoryService.getExpenseCategoriesByUserId(userId);
        grid.setItems(categories);
    }

    private void addCategory() {
        String name = nameField.getValue();

        if (name.trim().isEmpty()) {
            Notification.show("Category name cannot be empty", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        ExpenseCategory category = new ExpenseCategory();
        category.setName(name);
        category.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

        expenseCategoryService.addExpenseCategory(category);
        Notification.show("Category added successfully", 3000, Notification.Position.TOP_CENTER);
        clearForm();
        listCategories();
    }

    private void deleteCategory() {
        ExpenseCategory selectedCategory = grid.asSingleSelect().getValue();
        if (selectedCategory != null) {
            expenseCategoryService.deleteExpenseCategory(selectedCategory.getId());
            Notification.show("Category deleted successfully", 3000, Notification.Position.TOP_CENTER);
            listCategories();
        } else {
            Notification.show("Please select a category to delete", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        nameField.clear();
    }
}
