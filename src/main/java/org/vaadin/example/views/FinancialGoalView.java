package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;
import org.vaadin.example.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Route(value = "goals", layout = MainLayout.class)
@PageTitle("Manage Goals")
public class FinancialGoalView extends VerticalLayout {

    private final FinancialGoalService goalService;
    private final Grid<FinancialGoal> grid = new Grid<>(FinancialGoal.class);
    private final TextField descriptionField = new TextField("Goal Description");
    private final NumberField targetAmountField = new NumberField("Target Amount");
    private final Button saveButton = new Button("Save");

    @Autowired
    public FinancialGoalView(FinancialGoalService goalService) {
        this.goalService = goalService;
        setUpForm();
        setUpGrid();
        loadData();
    }

    private void setUpForm() {
        saveButton.addClickListener(e -> saveGoal());
        add(descriptionField, targetAmountField, saveButton);
    }

    private void setUpGrid() {
        grid.setColumns("id", "description", "targetAmount");
        grid.asSingleSelect().addValueChangeListener(e -> populateForm(e.getValue()));
        add(grid);
    }

    private void loadData() {
        grid.setItems(goalService.getAllGoals());
    }

    private void saveGoal() {
        FinancialGoal goal = new FinancialGoal();
        goal.setDescription(descriptionField.getValue());

        BigDecimal targetAmount = BigDecimal.valueOf(targetAmountField.getValue());
        goal.setTargetAmount(targetAmount);

        goalService.saveGoal(goal);
        loadData();
        Notification.show("Goal saved");
    }

    private void populateForm(FinancialGoal goal) {
        if (goal != null) {
            descriptionField.setValue(goal.getDescription());

            targetAmountField.setValue(goal.getTargetAmount().doubleValue());
        }
    }
}
