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

    public List<FinancialGoal> getFinancialGoalsByUserId(Long userId) {
        return financialGoalRepository.findByUserId(userId);
    }

    public FinancialGoal addFinancialGoal(FinancialGoal financialGoal) {
        return financialGoalRepository.save(financialGoal);
    }

    public FinancialGoal findFinancialGoalById(Long id) {
        return financialGoalRepository.findById(id).orElse(null);
    }

    public void deleteFinancialGoal(Long id) {
        financialGoalRepository.deleteById(id);
    }
}
