package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;

import java.util.List;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping("/user/{userId}")
    public List<Budget> getBudgetsByUserId(@PathVariable Long userId) {
        return budgetService.getBudgetsByUserId(userId);
    }

    @PostMapping("/add")
    public Budget addBudget(@RequestBody Budget budget) {
        return budgetService.addBudget(budget);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}
