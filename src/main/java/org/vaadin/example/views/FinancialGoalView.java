package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "goal", layout = MainLayout.class)
public class FinancialGoalView extends VerticalLayout {

    private final Grid<FinancialGoal> grid = new Grid<>(FinancialGoal.class);
    private final TextField descriptionField = new TextField("Goal Description");
    private final TextField targetAmountField = new TextField("Target Amount");

    private final FinancialGoalService financialGoalService;
    private final SessionService sessionService;
    private final UserService userService;

    public FinancialGoalView(FinancialGoalService financialGoalService, SessionService sessionService, UserService userService) {
        this.financialGoalService = financialGoalService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();

        Button addButton = new Button("Add Goal", event -> addGoal());
        Button deleteButton = new Button("Delete Goal", event -> deleteGoal());

        HorizontalLayout formLayout = new HorizontalLayout(descriptionField, targetAmountField, addButton, deleteButton);
        add(formLayout, grid);

        listGoals();
    }

    private void configureGrid() {
        grid.setColumns("description", "targetAmount");
    }

    private void listGoals() {
        Long userId = sessionService.getLoggedInUserId();
        List<FinancialGoal> goals = financialGoalService.getFinancialGoalsByUserId(userId);
        grid.setItems(goals);
    }

    private void addGoal() {
        String description = descriptionField.getValue();
        String targetAmountText = targetAmountField.getValue();
        BigDecimal targetAmount = new BigDecimal(targetAmountText);

        FinancialGoal goal = new FinancialGoal();
        goal.setDescription(description);
        goal.setTargetAmount(targetAmount);
        goal.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

        financialGoalService.addFinancialGoal(goal);
        Notification.show("Goal added successfully");
        listGoals();
        clearForm();
    }

    private void deleteGoal() {
        FinancialGoal selectedGoal = grid.asSingleSelect().getValue();
        if (selectedGoal != null) {
            financialGoalService.deleteFinancialGoal(selectedGoal.getId());
            Notification.show("Goal deleted successfully");
            listGoals();
        } else {
            Notification.show("Please select a goal to delete");
        }
    }

    private void clearForm() {
        descriptionField.clear();
        targetAmountField.clear();
    }
}
