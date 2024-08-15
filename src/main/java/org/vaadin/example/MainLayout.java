package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.BudgetView;
import org.vaadin.example.views.ExpenseView;
import org.vaadin.example.views.IncomeView;
import org.vaadin.example.views.FinancialGoalView;
import org.vaadin.example.views.DashboardView;
import org.vaadin.example.views.ExpenseCategoryView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Financial Tracker");
        logo.addClassNames("text-l", "m-m");
        addToNavbar(logo);
    }

    private void createDrawer() {
        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        RouterLink budgetLink = new RouterLink("Manage Budgets", BudgetView.class);
        RouterLink expenseLink = new RouterLink("Manage Expenses", ExpenseView.class);
        RouterLink incomeLink = new RouterLink("Manage Income", IncomeView.class);
        RouterLink goalLink = new RouterLink("Manage Goals", FinancialGoalView.class);
        RouterLink categoryLink = new RouterLink("Manage Categories", ExpenseCategoryView.class);

        addToDrawer(new VerticalLayout(
            dashboardLink,
            budgetLink,
            expenseLink,
            incomeLink,
            goalLink,
            categoryLink
        ));
    }
}
