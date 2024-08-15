package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Budget;
import org.vaadin.example.repository.BudgetRepository;

import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    public Budget addBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget findBudgetById(Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
