package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.repository.FinancialGoalRepository;

import java.util.List;

@Service
public class FinancialGoalService {
    @Autowired
    private FinancialGoalRepository financialGoalRepository;

    public List<FinancialGoal> getAllGoals() {
        return financialGoalRepository.findAll();
    }

    public FinancialGoal getGoalById(Long id) {
        return financialGoalRepository.findById(id).orElse(null);
    }

    public FinancialGoal saveGoal(FinancialGoal goal) {
        return financialGoalRepository.save(goal);
    }

    public void deleteGoal(Long id) {
        financialGoalRepository.deleteById(id);
    }
}
