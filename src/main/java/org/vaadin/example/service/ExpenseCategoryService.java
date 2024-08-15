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

    public List<ExpenseCategory> getExpenseCategoriesByUserId(Long userId) {
        return expenseCategoryRepository.findByUserId(userId);
    }

    public ExpenseCategory addExpenseCategory(ExpenseCategory expenseCategory) {
        return expenseCategoryRepository.save(expenseCategory);
    }

    public ExpenseCategory findExpenseCategoryById(Long id) {
        return expenseCategoryRepository.findById(id).orElse(null);
    }

    public void deleteExpenseCategory(Long id) {
        expenseCategoryRepository.deleteById(id);
    }
}
