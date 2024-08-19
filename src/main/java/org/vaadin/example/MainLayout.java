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
        H1 logo = new H1("FinTrack");
        logo.addClassNames("text-l", "m-m");


        Button logoutButton = new Button("Sign Out", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            VaadinSession.getCurrent().close();
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        Button budgetButton = new Button("Budgets");
        budgetButton.addClickListener(e -> budgetButton.getUI().ifPresent(ui -> ui.navigate(BudgetView.class)));

        Button expenseButton = new Button("Expenses");
        expenseButton.addClickListener(e -> expenseButton.getUI().ifPresent(ui -> ui.navigate(ExpenseView.class)));

        Button incomeButton = new Button("Income");
        incomeButton.addClickListener(e -> incomeButton.getUI().ifPresent(ui -> ui.navigate(IncomeView.class)));

        Button goalButton = new Button("Goals");
        goalButton.addClickListener(e -> goalButton.getUI().ifPresent(ui -> ui.navigate(FinancialGoalView.class)));

        Button dashboardButton = new Button("Dashboard");
        dashboardButton.addClickListener(e -> dashboardButton.getUI().ifPresent(ui -> ui.navigate(DashboardView.class)));


       HorizontalLayout header = new HorizontalLayout(logo, dashboardButton, budgetButton, expenseButton, incomeButton, goalButton, logoutButton);
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
   if (event.getNavigationTarget().equals(LoginView.class) || event.getNavigationTarget().equals(RegistrationView.class)) {
       return;
   }


   Long userId = (Long) VaadinSession.getCurrent().getAttribute("userId");


   if (userId == null) {
       event.forwardTo(LoginView.class); // Forward to the login view
   }
}


}
