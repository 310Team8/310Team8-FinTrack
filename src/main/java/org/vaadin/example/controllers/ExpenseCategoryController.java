package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;

import java.util.List;

@RestController
@RequestMapping("/expense-category")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @GetMapping("/user/{userId}")
    public List<ExpenseCategory> getExpenseCategoriesByUserId(@PathVariable Long userId) {
        return expenseCategoryService.getExpenseCategoriesByUserId(userId);
    }

    @PostMapping("/add")
    public ExpenseCategory addExpenseCategory(@RequestBody ExpenseCategory expenseCategory) {
        return expenseCategoryService.addExpenseCategory(expenseCategory);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteExpenseCategory(@PathVariable Long id) {
        expenseCategoryService.deleteExpenseCategory(id);
    }
}
