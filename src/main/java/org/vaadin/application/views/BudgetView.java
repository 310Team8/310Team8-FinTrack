package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Budget;
import org.vaadin.application.model.Expense;
import org.vaadin.application.service.BudgetService;
import org.vaadin.application.service.ExpenseService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

/**
 * The BudgetView class represents the user interface for managing budgets
 * within the application.
 * It provides functionality to add, display, and delete budgets. Each budget
 * can have a name, target
 * amount, and an associated icon. The class also displays the progress of
 * expenses against the budget.
 * 
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. The class uses various Vaadin
 * components like {@link com.vaadin.flow.component.textfield.TextField},
 * {@link com.vaadin.flow.component.combobox.ComboBox},
 * {@link com.vaadin.flow.component.button.Button},
 * {@link com.vaadin.flow.component.progressbar.ProgressBar}, and others to
 * create an interactive UI.
 * </p>
 * 
 * <p>
 * The {@code @Route} annotation maps this view to the "budget" URL path and
 * associates it with the
 * {@link org.vaadin.application.MainLayout}.
 * </p>
 * 
 * <p>
 * The class relies on several services:
 * {@link org.vaadin.application.service.BudgetService} for handling
 * budget data, {@link org.vaadin.application.service.ExpenseService} for
 * retrieving expenses related to a budget,
 * {@link org.vaadin.application.service.SessionService} for getting the
 * logged-in user ID, and
 * {@link org.vaadin.application.service.UserService} for retrieving user
 * information.
 * </p>
 * 
 * @see org.vaadin.application.service.BudgetService
 * @see org.vaadin.application.service.ExpenseService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "budget", layout = MainLayout.class)
public class BudgetView extends VerticalLayout {

    private final transient BudgetService budgetService;
    private final transient ExpenseService expenseService;
    private final transient SessionService sessionService;
    private final transient UserService userService;

    final TextField nameField = new TextField("Budget Name");
    final TextField amountField = new TextField("Target Amount ($)");
    private final ComboBox<String> iconComboBox = new ComboBox<>("Choose an Icon");

    private final Map<Budget, Div> budgetCards = new HashMap<>();
    private final Div budgetContainer = new Div();

    /**
     * Constructs a new BudgetView and initializes the components and layout.
     * 
     * @param budgetService  the service used to manage budget data
     * @param expenseService the service used to retrieve expenses data
     * @param sessionService the service used to manage session-related data,
     *                       particularly the logged-in user
     * @param userService    the service used to manage user data
     */
    public BudgetView(BudgetService budgetService, ExpenseService expenseService, SessionService sessionService,
            UserService userService) {
        this.budgetService = budgetService;
        this.expenseService = expenseService;
        this.sessionService = sessionService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);

        H2 title = new H2("My Budgets");
        title.addClassName("budget-title");

        configureInputFields();
        configureIconComboBox();

        Button addButton = new Button("Add Budget", event -> addBudget());
        addButton.addClassName("add-button");

        HorizontalLayout formLayout = new HorizontalLayout(nameField, amountField, iconComboBox, addButton);
        formLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        formLayout.setWidthFull();
        formLayout.setSpacing(true);

        budgetContainer.addClassName("budget-container");
        add(title, formLayout, budgetContainer);

        listBudgets();
    }

    /**
     * Configures the input fields for budget name and target amount.
     */
    private void configureInputFields() {
        nameField.setPlaceholder("Enter a name");
        amountField.setPlaceholder("Enter the target amount");
    }

    /**
     * Configures the ComboBox used to select an icon for the budget.
     */
    private void configureIconComboBox() {
        iconComboBox.setItems("💼", "🍽️", "🏠", "🚗");
        iconComboBox.setPlaceholder("Select an icon");
    }

    /**
     * Retrieves and lists all budgets for the currently logged-in user.
     */
    void listBudgets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        for (Budget budget : budgets) {
            Div budgetCard = createBudgetCard(budget);
            budgetCards.put(budget, budgetCard);
            budgetContainer.add(budgetCard);
        }
    }

    /**
     * Creates a visual card for the specified budget, showing its icon, name,
     * target amount,
     * total expenses so far, and a progress bar indicating how much of the budget
     * has been spent.
     * 
     * @param budget the budget to create a card for
     * @return a Div containing the visual representation of the budget
     */
    private Div createBudgetCard(Budget budget) {
        Div card = new Div();
        card.addClassName("budget-card");

        String icon = budget.getIcon() != null ? budget.getIcon() : "💼";

        Div iconDiv = new Div();
        iconDiv.setText(icon);
        iconDiv.addClassName("budget-icon");

        Div titleDiv = new Div();
        titleDiv.setText(budget.getName());
        titleDiv.addClassName("budget-title");

        Div targetDiv = new Div();
        targetDiv.setText("Target: $" + budget.getAmount());
        targetDiv.addClassName("budget-target");

        BigDecimal totalExpenses = getTotalExpensesForBudget(budget);

        Div currentAmountDiv = new Div();
        currentAmountDiv.setText("Spent so far: $" + totalExpenses.toString());
        currentAmountDiv.addClassName("current-amount");

        ProgressBar progressBar = new ProgressBar();
        progressBar.setWidth("100%");

        if (totalExpenses.compareTo(budget.getAmount()) > 0) {
            // Expenses exceed the budget
            progressBar.setValue(1.0); // Set to 100%
            progressBar.getElement().setAttribute("theme", "error");
            Notification.show("Warning: Budget exceeded!", 3000, Notification.Position.TOP_CENTER);
        } else if (budget.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progressValue = totalExpenses.divide(budget.getAmount(), 2, RoundingMode.HALF_UP);
            progressBar.setValue(progressValue.doubleValue());
        } else {
            progressBar.setValue(0.0);
        }

        Button deleteButton = new Button("Delete", event -> deleteBudget(budget));
        deleteButton.getStyle().set("background-color", "red");
        deleteButton.getStyle().set("color", "white");
        deleteButton.addClassName("delete-button");

        card.add(iconDiv, titleDiv, targetDiv, currentAmountDiv, progressBar, deleteButton);
        return card;
    }

    /**
     * Calculates the total expenses associated with the given budget.
     * 
     * @param budget the budget for which to calculate total expenses
     * @return the total amount of expenses as a BigDecimal
     */
    private BigDecimal getTotalExpensesForBudget(Budget budget) {
        List<Expense> expenses = expenseService.getExpensesByBudget(budget.getId());
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Adds a new budget based on the user input, saves it to the database, and
     * updates the UI
     * to display the new budget.
     * 
     * <p>
     * If the user inputs invalid amounts, a notification will be displayed.
     * </p>
     */
    void addBudget() {
        try {
            String name = nameField.getValue();
            String amountText = amountField.getValue();
            BigDecimal amount = new BigDecimal(amountText);

            String selectedIcon = iconComboBox.getValue();

            Budget budget = new Budget();
            budget.setName(name);
            budget.setAmount(amount);
            budget.setIcon(selectedIcon);
            budget.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

            Budget savedBudget = budgetService.addBudget(budget);
            Notification.show("Budget added successfully", 3000, Notification.Position.TOP_CENTER);

            clearForm();
            Div budgetCard = createBudgetCard(savedBudget);
            budgetCards.put(savedBudget, budgetCard);
            budgetContainer.add(budgetCard);
        } catch (NumberFormatException e) {
            Notification.show("Please enter valid amounts", 3000, Notification.Position.TOP_CENTER);
        }
    }

    /**
     * Deletes the specified budget from the database and removes its visual
     * representation
     * from the UI.
     * 
     * @param budget the budget to be deleted
     */
    void deleteBudget(Budget budget) {
        Div budgetCard = budgetCards.get(budget);
        if (budgetCard != null) {
            budgetService.deleteBudget(budget.getId());
            Notification.show("Budget deleted successfully", 3000, Notification.Position.TOP_CENTER);
            budgetContainer.remove(budgetCard);
            budgetCards.remove(budget);
        }
    }

    /**
     * Clears the input fields in the budget creation form.
     */
    void clearForm() {
        nameField.clear();
        amountField.clear();
        iconComboBox.clear();
    }
}
