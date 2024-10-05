package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Budget;
import org.vaadin.application.model.Expense;
import org.vaadin.application.service.BudgetService;
import org.vaadin.application.service.ExpenseService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

import com.vaadin.flow.component.combobox.ComboBox;

/**
 * The ExpenseView class provides the user interface for managing expenses
 * within the application.
 * Users can add, update, view, and delete expenses. The class also displays the
 * total expenses
 * for the current month and allows users to associate expenses with specific
 * budgets.
 * 
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It includes a
 * {@link com.vaadin.flow.component.grid.Grid}
 * to display the list of expenses,
 * {@link com.vaadin.flow.component.textfield.TextField} and
 * {@link com.vaadin.flow.component.datepicker.DatePicker} for user input, and
 * {@link com.vaadin.flow.component.combobox.ComboBox}
 * for selecting associated budgets.
 * </p>
 * 
 * <p>
 * The {@code @Route} annotation maps this view to the "expense" URL path and
 * associates
 * it with the {@link org.vaadin.application.MainLayout}.
 * </p>
 * 
 * <p>
 * This class interacts with the following services:
 * {@link org.vaadin.application.service.ExpenseService}
 * for managing expense data,
 * {@link org.vaadin.application.service.BudgetService} for managing budget
 * data,
 * {@link org.vaadin.application.service.SessionService} for managing
 * session-related data, and
 * {@link org.vaadin.application.service.UserService} for retrieving user
 * information.
 * </p>
 * 
 * @see org.vaadin.application.service.ExpenseService
 * @see org.vaadin.application.service.BudgetService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "expense", layout = MainLayout.class)
public class ExpenseView extends VerticalLayout {

    private final Grid<Expense> grid = new Grid<>(Expense.class);
    private final TextField descriptionField = new TextField("Description");
    private final TextField amountField = new TextField("Amount");
    private final DatePicker datePicker = new DatePicker("Date");
    private final ComboBox<Budget> budgetComboBox = new ComboBox<>("Select Budget");

    private final transient ExpenseService expenseService;
    private final transient SessionService sessionService;
    private final transient UserService userService;
    private final transient BudgetService budgetService;

    private H2 totalExpensesValue;
    private Div totalExpensesCard;
    private Expense selectedExpense;

    /**
     * Constructs a new ExpenseView and initializes the components and layout.
     * 
     * @param expenseService the service used to manage expense data
     * @param sessionService the service used to manage session-related data,
     *                       particularly the logged-in user
     * @param userService    the service used to manage user data
     * @param budgetService  the service used to manage budget data
     */
    public ExpenseView(ExpenseService expenseService, SessionService sessionService, UserService userService,
            BudgetService budgetService) {
        this.expenseService = expenseService;
        this.sessionService = sessionService;
        this.userService = userService;
        this.budgetService = budgetService;

        configureGrid();
        configureForm();

        Button addButton = new Button("Add/Update Expense", event -> addOrUpdateExpense());
        Button deleteButton = new Button("Delete Expense", event -> deleteExpense());

        HorizontalLayout formLayout = new HorizontalLayout(descriptionField, amountField, datePicker, budgetComboBox,
                addButton, deleteButton);
        formLayout.setWidthFull();
        formLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        totalExpensesValue = new H2("$ 0.00");
        totalExpensesCard = createDashboardCard("Total Expenses", totalExpensesValue);
    
        H1 logo = new H1("Expenses");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(logo, totalExpensesCard, formLayout, grid);
        mainLayout.setSpacing(false);

        add(mainLayout);

        listBudgets();
        listExpenses();
        updateTotalExpenses();
    }

    /**
     * Configures the grid to display the list of expenses, including the
     * description, amount,
     * date, and associated budget. Adds an "Actions" column with an edit button for
     * each expense.
     */

    private void configureGrid() {
        grid.setColumns("description", "amount", "date");

        grid.addColumn(expense -> {
            Budget budget = expense.getBudget();
            return budget != null ? budget.getName() : "No Budget";
        }).setHeader("Budget");

        grid.addComponentColumn(expense -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(event -> editExpense(expense));
            return editButton;
        }).setHeader("Actions");
    }

    /**
     * Configures the form fields for inputting expense data, including placeholders
     * for
     * description, amount, and date.
     */
    private void configureForm() {
        descriptionField.setPlaceholder("e.g., Groceries");
        amountField.setPlaceholder("e.g., 200.00");
        datePicker.setPlaceholder("Select a date");
    }

    /**
     * Fetches and lists the budgets available to the logged-in user in the
     * ComboBox.
     */
    private void listBudgets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        budgetComboBox.setItems(budgets);
        budgetComboBox.setItemLabelGenerator(Budget::getName);
    }

    /**
     * Fetches and lists the expenses for the currently logged-in user in the grid.
     */

    private void listExpenses() {
        Long userId = sessionService.getLoggedInUserId();
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        grid.setItems(expenses);
    }

    /**
     * Adds a new expense or updates an existing one based on the user input,
     * saves it to the database, and updates the grid to display the new or updated
     * expense.
     * 
     * <p>
     * If the user inputs invalid data or leaves required fields empty, a
     * notification is displayed.
     * </p>
     */
    private void addOrUpdateExpense() {
        String description = descriptionField.getValue();
        String amountText = amountField.getValue();
        BigDecimal amount = new BigDecimal(amountText);
        LocalDate date = datePicker.getValue();
        Budget selectedBudget = budgetComboBox.getValue();

        if (description.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0 || date == null) {
            Notification.show("Please fill in all required fields with valid data.");
            return;
        }

        if (selectedExpense == null) {
            addExpense(description, amount, date, selectedBudget);
        } else {
            updateExpense(selectedExpense, description, amount, date, selectedBudget);
        }
    }

    /**
     * Adds a new expense to the database and updates the associated budget's
     * current amount.
     * 
     * @param description    the description of the expense
     * @param amount         the amount of the expense
     * @param date           the date of the expense
     * @param selectedBudget the budget associated with the expense
     */
    private void addExpense(String description, BigDecimal amount, LocalDate date, Budget selectedBudget) {
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setDate(date != null ? java.sql.Date.valueOf(date) : null);
        expense.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
        expense.setBudget(selectedBudget);

        expenseService.addExpense(expense);
        Notification.show("Expense added successfully");

        if (selectedBudget != null) {
            selectedBudget.setCurrentAmount(selectedBudget.getCurrentAmount().add(amount));
            budgetService.addBudget(selectedBudget);
        }

        listExpenses();
        updateTotalExpenses();
        clearForm();
    }

    /**
     * Updates the details of an existing expense in the database and updates the
     * associated
     * budget's current amount.
     * 
     * @param expense        the expense to be updated
     * @param description    the updated description of the expense
     * @param amount         the updated amount of the expense
     * @param date           the updated date of the expense
     * @param selectedBudget the updated budget associated with the expense
     */
    private void updateExpense(Expense expense, String description, BigDecimal amount, LocalDate date,
            Budget selectedBudget) {
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setDate(date != null ? java.sql.Date.valueOf(date) : null);
        expense.setBudget(selectedBudget);

        expenseService.updateExpense(expense);
        Notification.show("Expense updated successfully");

        if (selectedBudget != null) {
            selectedBudget.setCurrentAmount(selectedBudget.getCurrentAmount().add(amount));
            budgetService.addBudget(selectedBudget);
        }

        listExpenses();
        updateTotalExpenses();
        clearForm();
        selectedExpense = null;
    }

    /**
     * Prepares the form with the selected expense's details for editing.
     * 
     * @param expense the expense to be edited
     */
    private void editExpense(Expense expense) {
        selectedExpense = expense;

        descriptionField.setValue(expense.getDescription());
        amountField.setValue(expense.getAmount().toString());
        datePicker.setValue(((Date) expense.getDate()).toLocalDate());
        budgetComboBox.setValue(expense.getBudget());
    }

    /**
     * Deletes the selected expense from the database and updates the associated
     * budget's
     * current amount. If no expense is selected, a notification is displayed
     * prompting the user to select one.
     */
    private void deleteExpense() {
        Expense selectedExpenseToDelete = grid.asSingleSelect().getValue();
        if (selectedExpenseToDelete != null) {
            Budget associatedBudget = selectedExpenseToDelete.getBudget();
            if (associatedBudget != null) {
                associatedBudget
                        .setCurrentAmount(
                                associatedBudget.getCurrentAmount().subtract(selectedExpenseToDelete.getAmount()));
                budgetService.addBudget(associatedBudget);
            }

            expenseService.deleteExpense(selectedExpenseToDelete.getId());
            Notification.show("Expense deleted successfully");
            listExpenses();
            updateTotalExpenses();
        } else {
            Notification.show("Please select an expense to delete");
        }
    }

    /**
     * Clears the input fields in the expense creation/update form.
     */
    private void clearForm() {
        descriptionField.clear();
        amountField.clear();
        datePicker.clear();
        budgetComboBox.clear();
        selectedExpense = null;
    }

    /**
     * Creates a dashboard card to display a specific title and value, used here to
     * show
     * total expenses for the current month.
     * 
     * @param title          the title of the dashboard card
     * @param valueComponent the value component to be displayed in the dashboard
     *                       card
     * @return a Div containing the visual representation of the dashboard card
     */
    private Div createDashboardCard(String title, H2 valueComponent) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        H3 cardTitle = new H3(title);
        cardTitle.addClassName("card-title");

        valueComponent.addClassName("card-value");

        card.add(cardTitle, valueComponent);
        return card;
    }

    /**
     * Updates the total expenses displayed on the dashboard by calculating the sum
     * of all expenses for the currently logged-in user.
     */
    private void updateTotalExpenses() {
        Long userId = sessionService.getLoggedInUserId();
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);
        BigDecimal totalExpenses = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalExpensesValue.setText("$ " + totalExpenses.toString());
    }
}
