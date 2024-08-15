package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.*;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Financial Tracker");
        logo.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Sign Out", event -> {
            getUI().ifPresent(ui -> {
                // Invalidate the session and navigate to the login view
                ui.getSession().close();
                ui.navigate("login");
            });
        });

        addToNavbar(logo, logoutButton);
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
