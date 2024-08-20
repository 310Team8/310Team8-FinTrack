package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;
import com.vaadin.flow.component.notification.Notification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "budget", layout = MainLayout.class)
public class BudgetView extends VerticalLayout {

    private final BudgetService budgetService;
    private final SessionService sessionService;
    private final UserService userService;
    

    private final TextField nameField = new TextField("Budget Name");
    private final TextField amountField = new TextField("Target Amount ($)");
    private final ComboBox<String> iconComboBox = new ComboBox<>("Choose an Icon");
    private final TextField currentAmountField = new TextField("Amount Saved($)");

    // Map to keep track of the budget card components
    private final Map<Budget, Div> budgetCards = new HashMap<>();

    // Container for budget cards
    private final Div budgetContainer = new Div();

    public BudgetView(BudgetService budgetService, SessionService sessionService, UserService userService) {
        this.budgetService = budgetService;
        this.sessionService = sessionService;
        this.userService = userService;

        setAlignItems(Alignment.CENTER);

        H2 title = new H2("My Budgets");
        title.addClassName("budget-title");

        configureInputFields();
        configureIconComboBox();

        Button addButton = new Button("Add Budget", event -> addBudget());
        addButton.addClassName("add-button");

        HorizontalLayout formLayout = new HorizontalLayout(nameField, amountField, currentAmountField, iconComboBox, addButton);
        formLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);
        formLayout.setWidthFull();
        formLayout.setSpacing(true);

        budgetContainer.addClassName("budget-container");
        add(title, formLayout, budgetContainer);

        listBudgets();
    }

    
    private void configureInputFields() {
        nameField.setPlaceholder("Enter a name");
        amountField.setPlaceholder("Enter the target amount");
        currentAmountField.setPlaceholder("Enter the current amount saved");
    }
    private void configureIconComboBox() {
        // Example icons, these could be image paths or icon names
        iconComboBox.setItems("💼", "🍽️", "🏠", "🚗");
        iconComboBox.setPlaceholder("Select an icon");
    }

    private void listBudgets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        for (Budget budget : budgets) {
            Div budgetCard = createBudgetCard(budget);
            budgetCards.put(budget, budgetCard);
            budgetContainer.add(budgetCard);
        }
    }

    private Div createBudgetCard(Budget budget) {
        Div card = new Div();
        card.addClassName("budget-card");
    
        String icon = budget.getIcon() != null ? budget.getIcon() : "💼"; // Use the stored icon or default to the bag icon
    
        Div iconDiv = new Div();
        iconDiv.setText(icon);
        iconDiv.addClassName("budget-icon");
    
        Div titleDiv = new Div();
        titleDiv.setText(budget.getName());
        titleDiv.addClassName("budget-title");
    
        Div targetDiv = new Div();
        targetDiv.setText("Target: $" + budget.getAmount());
        targetDiv.addClassName("budget-target");
    
        Div currentAmountDiv = new Div();
        BigDecimal currentAmount = budget.getCurrentAmount();
        currentAmountDiv.setText("Saved so far: $" + (currentAmount != null ? currentAmount.toString() : "0.00"));
        currentAmountDiv.addClassName("current-amount");
    
        ProgressBar progressBar = new ProgressBar();
        progressBar.setWidth("100%");
    
        if (currentAmount != null && budget.getAmount() != null && budget.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progressValue = currentAmount.divide(budget.getAmount(), 2, RoundingMode.HALF_UP);
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
    

    private void addBudget() {
        try {
            String name = nameField.getValue();
            String amountText = amountField.getValue();
            BigDecimal amount = new BigDecimal(amountText);
    
            String currentAmountText = currentAmountField.getValue();
            BigDecimal currentAmount = new BigDecimal(currentAmountText); // Parse the current amount
    
            String selectedIcon = iconComboBox.getValue(); // Get the selected icon
    
            Budget budget = new Budget();
            budget.setName(name);
            budget.setAmount(amount);
            budget.setIcon(selectedIcon);
            budget.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
            
            // Assuming you have a method to set the current amount saved in your budget entity
            budget.setCurrentAmount(currentAmount); 
    
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
    

    private void deleteBudget(Budget budget) {
        Div budgetCard = budgetCards.get(budget);
        if (budgetCard != null) {
            budgetService.deleteBudget(budget.getId());
            Notification.show("Budget deleted successfully", 3000, Notification.Position.TOP_CENTER);
            budgetContainer.remove(budgetCard);
            budgetCards.remove(budget);  // Remove the reference from the map
        }
    }

    private void clearForm() {
        nameField.clear();
        amountField.clear();
        currentAmountField.clear(); // Clear the current amount field
        iconComboBox.clear();
    }
    
    
}
