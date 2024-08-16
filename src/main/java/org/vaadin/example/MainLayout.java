package org.vaadin.example;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.views.DashboardView;
import org.vaadin.example.views.BudgetView;
import org.vaadin.example.views.ExpenseView;
import org.vaadin.example.views.IncomeView;
import org.vaadin.example.views.FinancialGoalView;
import org.vaadin.example.views.ExpenseCategoryView;
import org.vaadin.example.views.LoginView;
import org.vaadin.example.views.RegistrationView;

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Financial Tracker");
        logo.addClassNames("text-l", "m-m");

        Button logoutButton = new Button("Sign Out", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout header = new HorizontalLayout(logo, logoutButton);
        header.expand(logo);
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
        RouterLink budgetLink = new RouterLink("Manage Budgets", BudgetView.class);
        RouterLink expenseLink = new RouterLink("Manage Expenses", ExpenseView.class);
        RouterLink incomeLink = new RouterLink("Manage Income", IncomeView.class);
        RouterLink goalLink = new RouterLink("Manage Goals", FinancialGoalView.class);
        RouterLink categoryLink = new RouterLink("Manage Categories", ExpenseCategoryView.class);

        VerticalLayout drawerLayout = new VerticalLayout(
            dashboardLink,
            budgetLink,
            expenseLink,
            incomeLink,
            goalLink,
            categoryLink
        );
        
        drawerLayout.setAlignItems(Alignment.STRETCH);
        addToDrawer(drawerLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Allow access to login and register views without authentication
        if (event.getNavigationTarget().equals(LoginView.class) || event.getNavigationTarget().equals(RegistrationView.class)) {
            return;
        }

        // Check if the user is logged in
        Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");
        System.out.println("BeforeEnter: userId = " + userId);

        // Redirect to login page if not logged in and trying to access any other view
        if (userId == null) {
            event.forwardTo("");
        }
    }
}
