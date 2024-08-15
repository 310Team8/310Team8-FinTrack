package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.Income;
import org.vaadin.example.service.IncomeService;

@Controller
@RequestMapping("/incomes")
public class IncomeController {
    @Autowired
    private IncomeService incomeService;

    @GetMapping
    public String listIncomes(Model model) {
        model.addAttribute("incomes", incomeService.getAllIncomes());
        return "incomes";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("income", new Income());
        return "income_form";
    }

    @PostMapping("/add")
    public String addIncome(@ModelAttribute Income income) {
        incomeService.saveIncome(income);
        return "redirect:/incomes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("income", incomeService.getIncomeById(id));
        return "income_form";
    }

    @PostMapping("/edit/{id}")
    public String updateIncome(@PathVariable("id") Long id, @ModelAttribute Income income) {
        income.setId(id);
        incomeService.saveIncome(income);
        return "redirect:/incomes";
    }

    @GetMapping("/delete/{id}")
    public String deleteIncome(@PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
        return "redirect:/incomes";
    }
}
