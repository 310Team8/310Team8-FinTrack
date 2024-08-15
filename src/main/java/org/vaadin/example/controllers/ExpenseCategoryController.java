package org.vaadin.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.service.ExpenseCategoryService;

@Controller
@RequestMapping("/categories")
public class ExpenseCategoryController {
    @Autowired
    private ExpenseCategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new ExpenseCategory());
        return "category_form";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute ExpenseCategory category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "category_form";
    }

    @PostMapping("/edit/{id}")
    public String updateCategory(@PathVariable("id") Long id, @ModelAttribute ExpenseCategory category) {
        category.setId(id);
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}
