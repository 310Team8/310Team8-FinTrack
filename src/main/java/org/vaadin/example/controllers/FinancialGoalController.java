package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;

import java.util.List;

@RestController
@RequestMapping("/financial-goal")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService financialGoalService;

    @GetMapping("/user/{userId}")
    public List<FinancialGoal> getFinancialGoalsByUserId(@PathVariable Long userId) {
        return financialGoalService.getFinancialGoalsByUserId(userId);
    }

    @PostMapping("/add")
    public FinancialGoal addFinancialGoal(@RequestBody FinancialGoal financialGoal) {
        return financialGoalService.addFinancialGoal(financialGoal);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteFinancialGoal(@PathVariable Long id) {
        financialGoalService.deleteFinancialGoal(id);
    }
}
