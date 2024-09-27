package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

import org.vaadin.application.MainLayout;
import org.vaadin.application.model.ExpenseCategory;
import org.vaadin.application.service.ExpenseCategoryService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

/**
 * The ExpenseCategoryView class provides the user interface for managing expense categories
 * within the application. Users can add, view, and delete expense categories.
 * 
 * <p>This class extends {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It includes a {@link com.vaadin.flow.component.grid.Grid}
 * to display the list of categories, and {@link com.vaadin.flow.component.textfield.TextField} along
 * with {@link com.vaadin.flow.component.button.Button} for user input and actions.</p>
 * 
 * <p>The {@code @Route} annotation maps this view to the "category" URL path and associates 
 * it with the {@link org.vaadin.application.MainLayout}.</p>
 * 
 * <p>This class interacts with the following services: {@link org.vaadin.application.service.ExpenseCategoryService}
 * for managing expense category data, {@link org.vaadin.application.service.SessionService} for managing session-related data,
 * and {@link org.vaadin.application.service.UserService} for retrieving user information.</p>
 * 
 * @see org.vaadin.application.service.ExpenseCategoryService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "category", layout = MainLayout.class)
public class ExpenseCategoryView extends VerticalLayout {

    private final Grid<ExpenseCategory> grid = new Grid<>(ExpenseCategory.class);
    private final TextField nameField = new TextField("Category Name");

    private final ExpenseCategoryService expenseCategoryService;
    private final SessionService sessionService;
    private final UserService userService;

     /**
     * Constructs a new ExpenseCategoryView and initializes the components and layout.
     * 
     * @param expenseCategoryService the service used to manage expense category data
     * @param sessionService the service used to manage session-related data, particularly the logged-in user
     * @param userService the service used to manage user data
     */
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

    /**
     * Configures the grid to display the list of expense categories, excluding certain fields
     * like id and user.
     */
    private void configureGrid() {
        // Exclude id and user fields from being displayed
        grid.setColumns("name");
    }

    /**
     * Fetches and lists the expense categories for the currently logged-in user.
     */
    private void listCategories() {
        Long userId = sessionService.getLoggedInUserId();
        List<ExpenseCategory> categories = expenseCategoryService.getExpenseCategoriesByUserId(userId);
        grid.setItems(categories);
    }

    /**
     * Adds a new expense category based on the user input, saves it to the database, 
     * and updates the grid to display the new category.
     * 
     * <p>If the user inputs an empty category name, a notification will be displayed.</p>
     */
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

    /**
     * Deletes the selected expense category from the database and updates the grid.
     * 
     * <p>If no category is selected, a notification will be displayed prompting the user to select a category.</p>
     */
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

    /**
     * Clears the input field in the category creation form.
     */
    private void clearForm() {
        nameField.clear();
    }
}
