package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;

import org.vaadin.example.MainLayout;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * The FinancialGoalView class provides the user interface for managing financial goals within the application.
 * Users can add, view, update, and delete financial goals. The class also displays the user's financial goals
 * in a visually appealing card layout.
 * 
 * <p>This class extends {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It uses a {@link com.vaadin.flow.component.orderedlayout.FlexLayout}
 * to display financial goals in a flexible, responsive layout, and various Vaadin components like 
 * {@link com.vaadin.flow.component.textfield.TextField}, {@link com.vaadin.flow.component.textfield.NumberField},
 * and {@link com.vaadin.flow.component.progressbar.ProgressBar} to create an interactive user interface.</p>
 * 
 * <p>The {@code @Route} annotation maps this view to the "goal" URL path and associates 
 * it with the {@link org.vaadin.example.MainLayout}.</p>
 * 
 * <p>This class interacts with the following services: {@link org.vaadin.example.service.FinancialGoalService}
 * for managing financial goal data, {@link org.vaadin.example.service.SessionService} for managing session-related data,
 * and {@link org.vaadin.example.service.UserService} for retrieving user information.</p>
 * 
 * @see org.vaadin.example.service.FinancialGoalService
 * @see org.vaadin.example.service.SessionService
 * @see org.vaadin.example.service.UserService
 */

@Route(value = "goal", layout = MainLayout.class)
public class FinancialGoalView extends VerticalLayout {

    private final FlexLayout goalLayout = new FlexLayout();
    private final TextField descriptionField = new TextField("Goal Description");
    private final NumberField targetAmountField = new NumberField("Target Amount ($)");
    private final NumberField savedAmountField = new NumberField("Saved Amount ($)");

    private final FinancialGoalService financialGoalService;
    private final SessionService sessionService;
    private final UserService userService;

    /**
     * Constructs a new FinancialGoalView and initializes the components and layout.
     * 
     * @param financialGoalService the service used to manage financial goal data
     * @param sessionService the service used to manage session-related data, particularly the logged-in user
     * @param userService the service used to manage user data
     */
    public FinancialGoalView(FinancialGoalService financialGoalService, SessionService sessionService, UserService userService) {
        this.financialGoalService = financialGoalService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureLayout();
        createLayout();
        listGoals();
    }

    /**
     * Configures the layout of the financial goals section, ensuring a responsive design with
     * flexible wrapping and centered content.
     */
    private void configureLayout() {
        goalLayout.setWidthFull();
        goalLayout.setFlexWrap(FlexWrap.WRAP);
        goalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        goalLayout.getStyle().set("gap", "20px");
    }

    /**
     * Creates the main layout for the view, including the title, form for adding financial goals,
     * and the container for displaying the list of goals.
     */
    private void createLayout() {
        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setPadding(false);
        setSpacing(false);

        H1 title = new H1("My Financial Goals");
        title.getStyle().set("color", "#333").set("font-weight", "600").set("font-size", "2em");

        descriptionField.setPlaceholder("Enter goal description...");
        descriptionField.getStyle().set("width", "300px")
                .set("border-radius", "5px")
                .set("background-color", "#ffffff")
                .set("border", "1px solid #ccc")
                .set("padding", "10px");

        targetAmountField.setPlaceholder("Enter target amount...");
        targetAmountField.getStyle().set("width", "150px")
                .set("border-radius", "5px")
                .set("background-color", "#ffffff")
                .set("border", "1px solid #ccc")
                .set("padding", "10px");

        savedAmountField.setPlaceholder("Enter saved amount...");
        savedAmountField.getStyle().set("width", "150px")
                .set("border-radius", "5px")
                .set("background-color", "#ffffff")
                .set("border", "1px solid #ccc")
                .set("padding", "10px");

        Button addGoalButton = new Button("Add Goal", VaadinIcon.PLUS.create());
        addGoalButton.getStyle().set("background-color", "#007bff")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "10px 20px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        addGoalButton.addClickListener(event -> addGoal());

        HorizontalLayout formLayout = new HorizontalLayout(descriptionField, targetAmountField, savedAmountField, addGoalButton);
        formLayout.setWidthFull();
        formLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        formLayout.setAlignItems(Alignment.CENTER);

        VerticalLayout mainLayout = new VerticalLayout(title, formLayout, goalLayout);
        mainLayout.setSizeFull();
        mainLayout.getStyle().set("background-color", "#ffffff");

        add(mainLayout);
    }

    /**
     * Fetches and lists the financial goals for the currently logged-in user in the goal layout.
     * If no goals are found, a notification is displayed.
     */
    private void listGoals() {
        Long userId = sessionService.getLoggedInUserId();
        List<FinancialGoal> goals = financialGoalService.getFinancialGoalsByUserId(userId);

        goalLayout.removeAll();

        if (goals == null || goals.isEmpty()) {
            Notification.show("No financial goals found.", 3000, Notification.Position.TOP_CENTER);
        } else {
            for (FinancialGoal goal : goals) {
                goalLayout.add(createGoalCard(goal));
            }
        }
    }

    /**
     * Creates a visual card for a given financial goal, displaying its description, target amount,
     * saved amount, and progress towards the target. The card also includes buttons for editing and deleting the goal.
     * 
     * @param goal the financial goal to create a card for
     * @return a VerticalLayout representing the goal card
     */
    private VerticalLayout createGoalCard(FinancialGoal goal) {
        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.setWidth("300px");
        cardLayout.getStyle().set("border", "1px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("box-shadow", "0px 4px 8px rgba(0, 0, 0, 0.1)")
                .set("background-color", "#ffffff");
    
        Icon goalIcon = getGoalIcon(goal);
        goalIcon.setSize("24px");
        goalIcon.getStyle().set("color", "#007bff");
    
        Span description = new Span(goal.getDescription());
        description.getStyle().set("font-weight", "500")
                .set("font-size", "1.1em")
                .set("color", "#333")
                .set("white-space", "normal") 
                .set("overflow", "hidden") 
                .set("text-overflow", "ellipsis") 
                .set("width", "calc(100% - 40px)")
                .set("word-break", "break-word");
    
        Span targetAmount = new Span("Target: $" + goal.getTargetAmount());
        targetAmount.getStyle().set("color", "#666")
                .set("font-weight", "400")
                .set("font-size", "1em");
    
        Span savedAmount = new Span("Saved: $" + goal.getSavedAmount());
        savedAmount.getStyle().set("color", "#666")
                .set("font-weight", "400")
                .set("font-size", "1em");
    
        ProgressBar progressBar = new ProgressBar();
        BigDecimal progress = goal.getSavedAmount().divide(goal.getTargetAmount(), RoundingMode.HALF_UP);
        progressBar.setValue(progress.doubleValue());
        progressBar.setHeight("8px");
        progressBar.getStyle().set("margin-top", "10px")
                .set("border-radius", "4px")
                .set("background-color", "#e0e0e0");
    
        Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
        deleteButton.getStyle().set("background-color", "#dc3545")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");
    
        deleteButton.addClickListener(event -> {
            financialGoalService.deleteFinancialGoal(goal.getId());
            Notification.show("Goal deleted successfully", 3000, Notification.Position.TOP_CENTER);
            listGoals();
        });
    
        Button editButton = new Button("Edit", VaadinIcon.EDIT.create());
        editButton.getStyle().set("background-color", "#28a745")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");
    
        editButton.addClickListener(event -> showEditDialog(goal));
    
        HorizontalLayout topLayout = new HorizontalLayout(goalIcon, description);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.START); // Change to START to wrap text correctly
        topLayout.setWidth("100%");
    
        cardLayout.add(topLayout, targetAmount, savedAmount, progressBar, editButton, deleteButton);
        return cardLayout;
    }
    
    /**
     * Displays a dialog allowing the user to edit an existing financial goal.
     * 
     * @param goal the financial goal to be edited
     */
    private void showEditDialog(FinancialGoal goal) {
        Dialog editDialog = new Dialog();
        editDialog.setWidth("400px");
    
        TextField descriptionField = new TextField("Goal Description");
        descriptionField.setValue(goal.getDescription());
        descriptionField.setReadOnly(false); 
    
        NumberField targetAmountField = new NumberField("Target Amount ($)");
        targetAmountField.setValue(goal.getTargetAmount().doubleValue());
        targetAmountField.setReadOnly(false);
    
        NumberField savedAmountField = new NumberField("Saved Amount ($)");
        savedAmountField.setValue(goal.getSavedAmount().doubleValue());
    
        Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
        saveButton.getStyle().set("background-color", "#007bff")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");
    
        saveButton.addClickListener(event -> {
            String newDescription = descriptionField.getValue();

            if(savedAmountField.isEmpty() || targetAmountField.isEmpty()){
                Notification.show("Invalid input(s). Ensure all fields are correctly filled.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
            BigDecimal newTargetAmount = BigDecimal.valueOf(targetAmountField.getValue());
            BigDecimal newSavedAmount = BigDecimal.valueOf(savedAmountField.getValue());
    
            if (newDescription.isEmpty() || newTargetAmount.compareTo(BigDecimal.ZERO) <= 0 || newSavedAmount.compareTo(newTargetAmount) > 0 || newSavedAmount.compareTo(BigDecimal.ZERO) < 0) {
                Notification.show("Invalid input(s). Ensure all fields are correctly filled.", 3000, Notification.Position.TOP_CENTER);
                return;
            }
    
            goal.setDescription(newDescription);
            goal.setTargetAmount(newTargetAmount);
            goal.setSavedAmount(newSavedAmount);
    
            financialGoalService.updateFinancialGoal(goal);
    
            Notification.show("Goal updated successfully", 3000, Notification.Position.TOP_CENTER);
            listGoals();
            editDialog.close();
        });
    
        Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());
        cancelButton.getStyle().set("background-color", "#dc3545")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");
    
        cancelButton.addClickListener(event -> editDialog.close());
    
        VerticalLayout dialogLayout = new VerticalLayout(descriptionField, targetAmountField, savedAmountField, new HorizontalLayout(saveButton, cancelButton));
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
    
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    /**
     * Retrieves the appropriate icon for a financial goal. Currently, it always returns a coin pile icon.
     * 
     * @param goal the financial goal for which to get an icon
     * @return the icon representing the financial goal
     */
    private Icon getGoalIcon(FinancialGoal goal) {
        return VaadinIcon.COIN_PILES.create();
    }

    /**
     * Adds a new financial goal based on the user input, saves it to the database, and updates the goal layout to display the new goal.
     * Validates the input to ensure the description is not empty, the target amount is positive, 
     * and the saved amount is not greater than the target amount.
     */
    private void addGoal() {
        String description = descriptionField.getValue();
        BigDecimal targetAmount = targetAmountField.getValue() != null ? BigDecimal.valueOf(targetAmountField.getValue()) : BigDecimal.ZERO;
        BigDecimal savedAmount = savedAmountField.getValue() != null ? BigDecimal.valueOf(savedAmountField.getValue()) : BigDecimal.ZERO;

        if (description.isEmpty() || targetAmount.compareTo(BigDecimal.ZERO) <= 0 || savedAmount.compareTo(BigDecimal.ZERO) < 0 || targetAmountField.isEmpty() || savedAmountField.isEmpty()) {
            Notification.show("Please enter a valid goal description, target amount and saved amount", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        if (savedAmount.compareTo(targetAmount) > 0) {
            Notification.show("Saved amount cannot be greater than target amount.", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        FinancialGoal goal = new FinancialGoal();
        goal.setDescription(description);
        goal.setTargetAmount(targetAmount);
        goal.setSavedAmount(savedAmount);
        goal.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

        financialGoalService.addFinancialGoal(goal);
        Notification.show("Goal added successfully", 3000, Notification.Position.TOP_CENTER);
        listGoals();
        clearForm();
    }

    /**
     * Clears the form fields used for adding or editing a financial goal.
     */
    private void clearForm() {
        descriptionField.clear();
        targetAmountField.clear();
        savedAmountField.clear();
    }
}
