package org.vaadin.application.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Fetch;
import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Expense;
import org.vaadin.application.model.ExpenseCategory;
import org.vaadin.application.model.Income;
import org.vaadin.application.service.ExpenseCategoryService;
import org.vaadin.application.service.ExpenseService;
import org.vaadin.application.service.IncomeService;

/**
 * The DashboardView class represents the dashboard page of the application,
 * providing
 * an overview of the user's financial status for the current month, including
 * total
 * expenses, total income, and a list of expense categories.
 * 
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout}
 * to organize the dashboard components vertically on the page. It uses various
 * Vaadin
 * components like {@link com.vaadin.flow.component.html.Div},
 * {@link com.vaadin.flow.component.html.H2},
 * {@link com.vaadin.flow.component.listbox.ListBox}, and
 * {@link com.vaadin.flow.component.orderedlayout.HorizontalLayout}
 * to create a visually appealing and interactive UI.
 * </p>
 * 
 * <p>
 * The {@code @Route} annotation maps this view to the "dashboard" URL path and
 * associates
 * it with the {@link org.vaadin.application.MainLayout}.
 * </p>
 * 
 * <p>
 * This class relies on several services:
 * {@link org.vaadin.application.service.ExpenseService}
 * for retrieving expense data, {@link org.vaadin.application.service.IncomeService}
 * for retrieving income data,
 * and {@link org.vaadin.application.service.ExpenseCategoryService} for retrieving
 * the user's expense categories.
 * </p>
 * 
 * @see org.vaadin.application.service.ExpenseService
 * @see org.vaadin.application.service.IncomeService
 * @see org.vaadin.application.service.ExpenseCategoryService
 */
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ExpenseCategoryService expenseCategoryService;

    /**
     * Constructs a new DashboardView and initializes the components and layout.
     * 
     * @param expenseService         the service used to manage expense data
     * @param incomeService          the service used to manage income data
     * @param expenseCategoryService the service used to manage expense category
     *                               data
     */
    public DashboardView(ExpenseService expenseService, IncomeService incomeService,
            ExpenseCategoryService expenseCategoryService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.expenseCategoryService = expenseCategoryService;

        addClassName("dashboard-view");
        Long currentUserId = (Long) VaadinSession.getCurrent().getAttribute("userId");

        // Fetch the total expenses and income for the current month
        BigDecimal totalExpenses = expenseService.getTotalExpensesForCurrentMonth(currentUserId);
        BigDecimal totalIncome = incomeService.getTotalIncomeForCurrentMonth(currentUserId);

        // Handle null values by assigning a default value of BigDecimal.ZERO
        if (totalExpenses == null) {
            totalExpenses = BigDecimal.ZERO;
        }

        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }

        // Dashboard title
        H2 dashboardTitle = new H2("Welcome to the Dashboard!");
        dashboardTitle.addClassName("dashboard-title");

        // Cards for Total Expenses and Total Income
        Div totalExpensesCard = createDashboardCard("Total Expenses for this Month", totalExpenses.toString());
        Div totalIncomeCard = createDashboardCard("Total Income for this Month", totalIncome.toString());

        // Layout for cards
        HorizontalLayout statsLayout = new HorizontalLayout(totalExpensesCard, totalIncomeCard);
        statsLayout.addClassName("dashboard-stats");

        // Create the category and expense lists
        VerticalLayout categoryLayout = createExpenseCategoryList(currentUserId);
        VerticalLayout expenseLayout = createExpenseList(currentUserId);

        // In your DashboardView or another relevant view
        HorizontalLayout categoryAndExpenseLayout = new HorizontalLayout(categoryLayout, expenseLayout);
        categoryAndExpenseLayout.addClassName("category-expense-layout");

    
         // Create the income list
         VerticalLayout incomeLayout = createIncomeList(currentUserId);
         incomeLayout.addClassName("income-full-row");
 
         // Add all components to the main layout
         add(dashboardTitle, statsLayout, categoryAndExpenseLayout, incomeLayout);


        

        // Additional dashboard components and features can be added here
    }

    /**
     * Creates a card displaying a title and a value. The card is used for
     * displaying
     * total expenses and total income in the dashboard.
     * 
     * @param title the title of the card
     * @param value the value to be displayed in the card
     * @return a Div containing the visual representation of the card
     */
    Div createDashboardCard(String title, String value) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        H2 cardTitle = new H2(title);
        cardTitle.addClassName("card-title");

        H2 cardValue = new H2("$" + value);
        cardValue.addClassName("card-value");

        card.add(cardTitle, cardValue);
        return card;
    }

    /**
     * Creates a layout that displays the user's expense categories in a list.
     * 
     * <p>
     * The categories are retrieved based on the user ID, and each category is
     * displayed
     * with a border line and padding.
     * </p>
     * 
     * @param userId the ID of the user for whom the categories are fetched
     * @return a VerticalLayout containing the expense categories
     */
    VerticalLayout createExpenseCategoryList(Long userId) {
        List<ExpenseCategory> categories = expenseCategoryService.getExpenseCategoriesByUserId(userId);

        // Title for the categories section
        H2 categoryTitle = new H2("Expense Categories");
        categoryTitle.addClassName("category-title");

        VerticalLayout categoryLayout = new VerticalLayout();
        categoryLayout.addClassName("category-layout");

        // Create a custom Div for each category
        for (ExpenseCategory category : categories) {
            Div categoryItem = new Div();
            categoryItem.addClassName("category-item");

            // Create a span for the category name
            Div name = new Div();
            name.setText(category.getName());
            name.addClassName("category-name");

            // Add the category name to the category item
            categoryItem.add(name);

            // Add the category item to the layout
            categoryLayout.add(categoryItem);
        }

        VerticalLayout mainLayout = new VerticalLayout(categoryTitle, categoryLayout);
        mainLayout.addClassName("category-list-box");
        return mainLayout;
    }

    /**
     * Creates a layout that displays the users's expense in a list
     * 
     * <p>
     * the expense are retrvied based on the user ID, each expense is displayed with
     * a border line and padding.
     * </p>
     * 
     * @param userId the ID of the user for whom the expenses are fetched
     * @return a VerticalLayout containing the expenses
     */
    private VerticalLayout createExpenseList(Long userId) {
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);

        // Title for the expenses section
        H2 expenseTitle = new H2("Expenses");
        expenseTitle.addClassName("expense-title");

        VerticalLayout expenseLayout = new VerticalLayout();
        expenseLayout.addClassName("expense-layout");

        // Create a custom Div for each expense
        for (Expense expense : expenses) {
            Div expenseItem = new Div();
            expenseItem.addClassName("expense-item");

            // Create a span for the description
            Div description = new Div();
            description.setText(expense.getDescription());
            description.addClassName("expense-description");

            // Create a span for the amount
            Div amount = new Div();
            amount.setText("$" + expense.getAmount().toString());
            amount.addClassName("expense-amount");

            // Add the description and amount to the expense item
            expenseItem.add(description, amount);

            // Add the expense item to the layout
            expenseLayout.add(expenseItem);
        }

        VerticalLayout mainLayout = new VerticalLayout(expenseTitle, expenseLayout);
        mainLayout.addClassName("expense-list-box");
        return mainLayout;
    }

    /**
     * Creates a layout that displays the users's income in a list
     * 
     * 
     * 
     * @param userId
     * @return
     */
    private VerticalLayout createIncomeList(Long userId) {
        List<Income> incomes = incomeService.getIncomesByUserId(userId);

        // Title for the incomes section
        H2 incomeTitle = new H2("Incomes");
        incomeTitle.addClassName("income-title");

        VerticalLayout incomeLayout = new VerticalLayout();
        incomeLayout.addClassName("income-layout");

        // Create a custom Div for each income
        for (Income income : incomes) {
            Div incomeItem = new Div();
            incomeItem.addClassName("income-item");

            // Use the "source" field as the description
            Div description = new Div();
            description.setText(income.getSource());
            description.addClassName("income-description");

            // Create a span for the amount
            Div amount = new Div();
            amount.setText("$" + income.getAmount().toString());
            amount.addClassName("income-amount");

            // Add the description and amount to the income item
            incomeItem.add(description, amount);

            // Add the income item to the layout
            incomeLayout.add(incomeItem);
        }

        VerticalLayout mainLayout = new VerticalLayout(incomeTitle, incomeLayout);
        mainLayout.addClassName("income-list-box");
        return mainLayout;
    }

}
