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
import org.vaadin.example.views.AssetView;
import org.vaadin.example.views.BudgetView;
import org.vaadin.example.views.ExpenseView;
import org.vaadin.example.views.IncomeView;
import org.vaadin.example.views.FinancialGoalView;
import org.vaadin.example.views.ExpenseCategoryView;
import org.vaadin.example.views.LoginView;
import org.vaadin.example.views.RegistrationView;

/**
 * The MainLayout class serves as the primary layout for the application, providing a common
 * structure that includes a header and a navigation drawer. The header contains the application logo 
 * and buttons for navigating between different views, as well as a logout button. The drawer contains 
 * links for managing various aspects of the application, such as budgets, expenses, income, goals, 
 * and categories.
 *
 * <p>This class extends {@link com.vaadin.flow.component.applayout.AppLayout}, which provides a 
 * pre-configured layout with a navigation bar and a drawer.</p>
 *
 * <p>The class implements {@link com.vaadin.flow.router.BeforeEnterObserver} to check the user's 
 * authentication status before entering certain views. If the user is not logged in, they are 
 * redirected to the login view.</p>
 *
 * @see com.vaadin.flow.component.applayout.AppLayout
 * @see com.vaadin.flow.router.BeforeEnterObserver
 */

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    /**
     * Constructs a new MainLayout and initializes the header and drawer components.
     */
   public MainLayout() {
       createHeader();
       createDrawer();
   }

   /**
     * Creates the header of the application, which includes the logo, navigation buttons,
     * and a logout button. The header is added to the navigation bar.
     */
   private void createHeader() {
        H1 logo = new H1("FinTrack");
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

   /**
     * Creates the drawer of the application, which contains links to various management views 
     * (Dashboard, Budgets, Expenses, Income, Goals, and Categories). The drawer is added to the 
     * application's main layout.
     */
   private void createDrawer() {
       RouterLink dashboardLink = new RouterLink("Dashboard", DashboardView.class);
       RouterLink budgetLink = new RouterLink("Manage Budgets", BudgetView.class);
       RouterLink expenseLink = new RouterLink("Manage Expenses", ExpenseView.class);
       RouterLink incomeLink = new RouterLink("Manage Income", IncomeView.class);
       RouterLink goalLink = new RouterLink("Manage Goals", FinancialGoalView.class);
       RouterLink categoryLink = new RouterLink("Manage Categories", ExpenseCategoryView.class);
       RouterLink assetLink = new RouterLink("Manage Assets", AssetView.class);


       VerticalLayout drawerLayout = new VerticalLayout(
           dashboardLink,
           budgetLink,
           expenseLink,
           incomeLink,
           goalLink,
           categoryLink,
           assetLink
       );
      
       drawerLayout.setAlignItems(Alignment.STRETCH);
       addToDrawer(drawerLayout);
   }

   /**
     * Before entering any view, this method checks if the user is logged in by verifying the presence 
     * of a user ID in the session. If the user is not logged in and attempts to access a secured view,
     * they are redirected to the login view.
     *
     * @param event the event triggered before entering the view
     */
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
