package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Income;
import org.vaadin.example.repository.IncomeRepository;

import java.util.List;

@Service
public class IncomeService {
    @Autowired
    private IncomeRepository incomeRepository;

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }
}
