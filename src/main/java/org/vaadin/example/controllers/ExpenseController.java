package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.Expense;
import org.vaadin.example.service.ExpenseService;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public String listExpenses(Model model) {
        model.addAttribute("expenses", expenseService.getAllExpenses());
        return "expenses";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "expense_form";
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense) {
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("expense", expenseService.getExpenseById(id));
        return "expense_form";
    }

    @PostMapping("/edit/{id}")
    public String updateExpense(@PathVariable("id") Long id, @ModelAttribute Expense expense) {
        expense.setId(id);
        expenseService.saveExpense(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
}
