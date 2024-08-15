package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;

@Controller
@RequestMapping("/budgets")
public class BudgetController {
    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public String listBudgets(Model model) {
        model.addAttribute("budgets", budgetService.getAllBudgets());
        return "budgetsList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("budget", new Budget());
        return "budget_form";
    }

    @PostMapping("/add")
    public String addBudget(@ModelAttribute Budget budget) {
        budgetService.saveBudget(budget);
        return "redirect:/budgets";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("budget", budgetService.getBudgetById(id));
        return "budget_form";
    }

    @PostMapping("/edit/{id}")
    public String updateBudget(@PathVariable("id") Long id, @ModelAttribute Budget budget) {
        budget.setId(id);
        budgetService.saveBudget(budget);
        return "redirect:/budgets";
    }

    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable("id") Long id) {
        budgetService.deleteBudget(id);
        return "redirect:/budgets";
    }
}
