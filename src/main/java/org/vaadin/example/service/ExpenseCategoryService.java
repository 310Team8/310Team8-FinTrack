package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.repository.ExpenseCategoryRepository;

import java.util.List;

@Service
public class ExpenseCategoryService {
    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    public List<ExpenseCategory> getAllCategories() {
        return expenseCategoryRepository.findAll();
    }

    public ExpenseCategory getCategoryById(Long id) {
        return expenseCategoryRepository.findById(id).orElse(null);
    }

    public ExpenseCategory saveCategory(ExpenseCategory category) {
        return expenseCategoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        expenseCategoryRepository.deleteById(id);
    }
}
