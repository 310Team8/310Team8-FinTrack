package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.service.FinancialGoalService;

@Controller
@RequestMapping("/goals")
public class FinancialGoalController {
    @Autowired
    private FinancialGoalService goalService;

    @GetMapping
    public String listGoals(Model model) {
        model.addAttribute("goals", goalService.getAllGoals());
        return "goals";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("goal", new FinancialGoal());
        return "goal_form";
    }

    @PostMapping("/add")
    public String addGoal(@ModelAttribute FinancialGoal goal) {
        goalService.saveGoal(goal);
        return "redirect:/goals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("goal", goalService.getGoalById(id));
        return "goal_form";
    }

    @PostMapping("/edit/{id}")
    public String updateGoal(@PathVariable("id") Long id, @ModelAttribute FinancialGoal goal) {
        goal.setId(id);
        goalService.saveGoal(goal);
        return "redirect:/goals";
    }

    @GetMapping("/delete/{id}")
    public String deleteGoal(@PathVariable("id") Long id) {
        goalService.deleteGoal(id);
        return "redirect:/goals";
    }
}
