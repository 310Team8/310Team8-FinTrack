package org.vaadin.example.views;

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
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;
import org.vaadin.example.service.ExpenseService;
import org.vaadin.example.service.IncomeService;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;
    private final ExpenseCategoryService expenseCategoryService;

    public DashboardView(ExpenseService expenseService, IncomeService incomeService, ExpenseCategoryService expenseCategoryService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
        this.expenseCategoryService = expenseCategoryService;

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

        add(dashboardTitle, statsLayout);

        // Fetch and display expense categories
        add(createExpenseCategoryList(currentUserId));

        // Additional dashboard components and features can be added here
    }

    private Div createDashboardCard(String title, String value) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        H2 cardTitle = new H2(title);
        cardTitle.addClassName("card-title");

        H2 cardValue = new H2("$" + value);
        cardValue.addClassName("card-value");

        card.add(cardTitle, cardValue);
        return card;
    }

    private VerticalLayout createExpenseCategoryList(Long userId) {
    List<ExpenseCategory> categories = expenseCategoryService.getExpenseCategoriesByUserId(userId);
    
    // Title for the categories section
    H2 categoryTitle = new H2("Expense Categories");
    categoryTitle.addClassName("category-title");

    // ListBox to display categories
    ListBox<String> categoryListBox = new ListBox<>();
    categoryListBox.addClassName("category-list-box");
    categoryListBox.setItems(categories.stream().map(ExpenseCategory::getName).toArray(String[]::new));

    // Custom item renderer with lines between categories
    categoryListBox.setRenderer(new TextRenderer<>(item -> item));
    categoryListBox.addAttachListener(event -> {
        for (Element item : categoryListBox.getElement().getChildren().collect(Collectors.toList())) {
            item.getStyle().set("border-bottom", "1px solid #ccc");
            item.getStyle().set("padding", "10px");
        }
    });

    // Layout to contain the title and ListBox
    VerticalLayout categoryLayout = new VerticalLayout(categoryTitle, categoryListBox);
    categoryLayout.addClassName("category-layout");

    return categoryLayout;
}
}
