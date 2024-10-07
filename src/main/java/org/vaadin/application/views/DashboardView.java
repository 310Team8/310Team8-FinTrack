package org.vaadin.application.views;

import com.helger.commons.annotation.OverrideOnDemand;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.PriorityQueue;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Expense;
import org.vaadin.application.model.ExpenseCategory;
import org.vaadin.application.model.Income;
import org.vaadin.application.model.SummaryFile;
import org.vaadin.application.model.TransactionRecord;
import org.vaadin.application.service.ExpenseCategoryService;
import org.vaadin.application.service.ExpenseService;
import org.vaadin.application.service.IncomeService;
import org.vaadin.application.service.UserService;

/**
 * The DashboardView class represents the dashboard page of the application,
 * providing an overview
 * of the user's financial status for the current month, including total
 * expenses, total income, and
 * a list of expense categories.
 *
 * <p>
 * This class extends
 * {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the dashboard components vertically on the page. It uses various Vaadin
 * components like {@link
 * com.vaadin.flow.component.html.Div},
 * {@link com.vaadin.flow.component.html.H2}, {@link
 * com.vaadin.flow.component.listbox.ListBox}, and {@link
 * com.vaadin.flow.component.orderedlayout.HorizontalLayout} to create a
 * visually appealing and
 * interactive UI.
 *
 * <p>
 * The {@code @Route} annotation maps this view to the "dashboard" URL path and
 * associates it
 * with the {@link org.vaadin.application.MainLayout}.
 *
 * <p>
 * This class relies on several services:
 * {@link org.vaadin.application.service.ExpenseService}
 * for retrieving expense data,
 * {@link org.vaadin.application.service.IncomeService} for retrieving
 * income data, and
 * {@link org.vaadin.application.service.ExpenseCategoryService} for retrieving
 * the
 * user's expense categories.
 *
 * @see org.vaadin.application.service.ExpenseService
 * @see org.vaadin.application.service.IncomeService
 * @see org.vaadin.application.service.ExpenseCategoryService
 */
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final transient ExpenseService expenseService;
    private final transient IncomeService incomeService;
    private final transient ExpenseCategoryService expenseCategoryService;
    private final transient UserService userService;

    /**
     * Constructs a new DashboardView and initializes the components and layout.
     *
     * @param expenseService         the service used to manage expense data
     * @param incomeService          the service used to manage income data
     * @param expenseCategoryService the service used to manage expense category
     *                               data
     */
    public DashboardView(
            ExpenseService expenseService,
            IncomeService incomeService,
            ExpenseCategoryService expenseCategoryService,
            UserService userService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.expenseCategoryService = expenseCategoryService;
        this.userService = userService;

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

        // PDF Button
        Button pdfButton = new Button("Generate Summary", event -> generateSummary(currentUserId));
        pdfButton.addClassName("pdf-button");

        HorizontalLayout headerLayout = new HorizontalLayout(dashboardTitle, pdfButton);
        headerLayout.addClassName("dashboard-header");

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
        add(headerLayout, statsLayout, categoryAndExpenseLayout, incomeLayout);

        // Additional dashboard components and features can be added here
    }

    /**
     * Creates a card displaying a title and a value. The card is used for
     * displaying total expenses
     * and total income in the dashboard.
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
     * displayed with a
     * border line and padding.
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
     * a border line
     * and padding.
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

    /**
     * Generates a PDF financial summary for the specified user
     *
     * @param userId
     */
    private void generateSummary(Long userId) {
        String currentUserName = userService.findUserById(userId).getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        BigDecimal currentMonthTotalIncome = BigDecimal.ZERO;
        BigDecimal pastSixMonthsTotalIncome = BigDecimal.ZERO;
        BigDecimal pastTwelveMonthsTotalIncome = BigDecimal.ZERO;

        BigDecimal currentMonthTotalExpense = expenseService.getTotalExpensesForCurrentMonth(userId);
        BigDecimal pastSixMonthsTotalExpense = expenseService.getTotalExpensesForPreviousMonths(userId, 5);
        BigDecimal pastTwelveMonthsTotalExpense = expenseService.getTotalExpensesForPreviousMonths(userId, 11);

        List<Income> incomes = incomeService.getIncomesByUserId(userId);
        List<Expense> expenses = expenseService.getExpensesByUserId(userId);

        // Sorts transactions by dates to determine table row order
        PriorityQueue<TransactionRecord> recordsQueue = new PriorityQueue<>(
                (r1, r2) -> r2.getTransactionDate().compareTo(r1.getTransactionDate()));

        for (Income income : incomes) {
            java.sql.Date utilDate = (java.sql.Date) income.getDate();
            LocalDate date = utilDate.toLocalDate();
            while (date.isBefore(LocalDate.now().withDayOfMonth(1).plusMonths(1))) {
                recordsQueue.add(
                        new TransactionRecord(date, income.getAmount(), income.getSource(), "Income"));

                if (date.isAfter(LocalDate.now().withDayOfMonth(1))) {
                    pastTwelveMonthsTotalIncome = pastTwelveMonthsTotalIncome.add(income.getAmount());
                    pastSixMonthsTotalIncome = pastSixMonthsTotalIncome.add(income.getAmount());
                    currentMonthTotalIncome = currentMonthTotalIncome.add(income.getAmount());
                } else if (date.isAfter(LocalDate.now().withDayOfMonth(1).minusMonths(5))) {
                    pastSixMonthsTotalIncome = pastSixMonthsTotalIncome.add(income.getAmount());
                    pastTwelveMonthsTotalIncome = pastTwelveMonthsTotalIncome.add(income.getAmount());
                } else {
                    pastTwelveMonthsTotalIncome = pastTwelveMonthsTotalIncome.add(income.getAmount());
                }

                // Calculates reoccuring incomes, database only stores one instance with the
                // payment frequency.
                if (income.getPaymentFrequency().equals("Weekly")) {
                    date = date.plusWeeks(1);
                } else if (income.getPaymentFrequency().equals("Biweekly")) {
                    date = date.plusWeeks(2);
                } else {
                    date = date.plusMonths(1);
                }
            }
        }

        for (Expense expense : expenses) {
            java.sql.Date utilDate = (java.sql.Date) expense.getDate();
            LocalDate date = utilDate.toLocalDate();
            if (date.isBefore(LocalDate.now().withDayOfMonth(1).plusMonths(1))) {
                recordsQueue.add(
                    new TransactionRecord(date, expense.getAmount(), expense.getDescription(), "Expense"));
            }
        }

        try {
            SummaryFile summaryFile = new SummaryFile();

            // Initializes Header Texts into string array as summaryfile.addLinesofText()
            // only takes string array as input
            String[] header = { currentUserName + "'s FINANCIAL REPORT" };
            String[] dateHeaders = {
                    "Current Month: "
                            + LocalDate.now().withDayOfMonth(1).format(formatter)
                            + " - "
                            + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(formatter),
                    "Past 6 Months: "
                            + LocalDate.now().withDayOfMonth(1).minusMonths(5).format(formatter)
                            + " - "
                            + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(formatter),
                    "Past 12 Months: "
                            + LocalDate.now().withDayOfMonth(1).minusMonths(11).format(formatter)
                            + " - "
                            + LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).format(formatter)
            };
            String[] currentMonthSummary = {
                    SummaryFile.INCOME_LABEL
                            + currentMonthTotalIncome.toString()
                            + SummaryFile.EXPENSE_LABEL
                            + currentMonthTotalExpense.toString()
                            + SummaryFile.NET_LABEL
                            + currentMonthTotalIncome.subtract(currentMonthTotalExpense).toString()
            };
            String[] pastSixMonthsSummary = {
                    SummaryFile.INCOME_LABEL
                            + pastSixMonthsTotalIncome.toString()
                            + SummaryFile.EXPENSE_LABEL
                            + pastSixMonthsTotalExpense.toString()
                            + SummaryFile.NET_LABEL
                            + pastSixMonthsTotalIncome.subtract(pastSixMonthsTotalExpense).toString()
            };
            String[] pastTwelveMonthsSummary = {
                    SummaryFile.INCOME_LABEL
                            + pastTwelveMonthsTotalIncome.toString()
                            + SummaryFile.EXPENSE_LABEL
                            + pastTwelveMonthsTotalExpense.toString()
                            + SummaryFile.NET_LABEL
                            + pastTwelveMonthsTotalIncome.subtract(pastTwelveMonthsTotalExpense).toString()
            };

            summaryFile.addLinesofText(
                    header,
                    new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),
                    28,
                    30,
                    summaryFile.getHeight() - 54,
                    14.5f,
                    Color.BLACK);
            summaryFile.addLinesofText(
                    dateHeaders,
                    new PDType1Font(Standard14Fonts.FontName.COURIER_BOLD),
                    16,
                    30,
                    summaryFile.getHeight() - 100,
                    60f,
                    Color.BLACK);
            summaryFile.addLinesofText(
                    currentMonthSummary,
                    new PDType1Font(Standard14Fonts.FontName.COURIER),
                    14,
                    30,
                    summaryFile.getHeight() - 120,
                    84f,
                    Color.BLACK);
            summaryFile.addLinesofText(
                    pastSixMonthsSummary,
                    new PDType1Font(Standard14Fonts.FontName.COURIER),
                    14,
                    30,
                    summaryFile.getHeight() - 180,
                    84f,
                    Color.BLACK);
            summaryFile.addLinesofText(
                    pastTwelveMonthsSummary,
                    new PDType1Font(Standard14Fonts.FontName.COURIER),
                    14,
                    30,
                    summaryFile.getHeight() - 240,
                    84f,
                    Color.BLACK);

            summaryFile.addRecords(recordsQueue);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            summaryFile.closeStream();
            PDDocument finalFile = summaryFile.getDocument();
            finalFile.save(outputStream);
            finalFile.close();

            Path path = Path.of("src/main/resources/static/summary.pdf");
            Files.write(path, outputStream.toByteArray());
            outputStream.close();
            System.out.println("PDF generated: " + path.toAbsolutePath());

            //Delay loading pdf file to account for file system delay. (Time taken for file to appear in folder).
            getUI().ifPresent(ui -> ui.access(() -> {
                ui.setPollInterval(1000);
                ui.addPollListener(e -> 
                    ui.access(() -> {
                        ui.getPage().open("/summary.pdf", "_blank");
                        ui.setPollInterval(-1);
                    })
                );
            }));
        } catch (IOException e) {
            System.out.println(SummaryFile.PDF_FAIL_STRING + e.getMessage());
        }
    }
}
