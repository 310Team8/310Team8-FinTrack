package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;

import java.util.List;

/**
 * Rest controller for managing financial goal-related operations.
 * This controller provides endpoints to get financial goals by user ID, 
 * add a new financial goal, and delete an existing financial goal.
 */
@RestController
@RequestMapping("/financial-goal")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    /**
     * Retrieves a list of financial goals associated with a specific user ID.
     *
     * @param userId the ID of the user whose financial goals are to be retrieved
     * @return a list of financial goals associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<FinancialGoal> getFinancialGoalsByUserId(@PathVariable Long userId) {
        return financialGoalService.getFinancialGoalsByUserId(userId);
    }

    /**
     * Adds a new financial goal.
     *
     * @param financialGoal the financial goal object to be added
     * @return the newly added financial goal object
     */
    @PostMapping("/add")
    public FinancialGoal addFinancialGoal(@RequestBody FinancialGoal financialGoal) {
        return financialGoalService.addFinancialGoal(financialGoal);
    }

     /**
     * Deletes a financial goal by its ID.
     *
     * @param id the ID of the financial goal to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteFinancialGoal(@PathVariable Long id) {
        financialGoalService.deleteFinancialGoal(id);
    }
}
