package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.aspectj.lang.annotation.Before;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.Budget;
import org.vaadin.example.repository.BudgetRepository;

public class BudgetServiceTests {

    @Mock 
    private BudgetRepository budgetRepository;

    @InjectMocks 
    private BudgetService budgetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBudgetsByUserId() {
        Long userId = 1L;
        Budget budget1 = new Budget();
        budget1.setId(1L);
        budget1.setName("Groceries");
        budget1.setAmount(new BigDecimal("500.00"));
        budget1.setId(userId);

        Budget budget2 = new Budget();
        budget2.setId(2L);
        budget2.setName("Utilities");
        budget2.setAmount(new BigDecimal("200.00"));
        budget2.setId(userId);

        List<Budget> budgets = Arrays.asList(budget1, budget2);

        when(budgetRepository.findByUserId(userId)).thenReturn(budgets);

        List<Budget> result = budgetService.getBudgetsByUserId(userId);
        assertEquals(2, result.size());
        verify(budgetRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testAddExpense() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("Groceries");
        budget.setAmount(new BigDecimal("500.00"));
        budget.setId(1L);

        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.addBudget(budget);
        assertEquals(budget, result);
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testFindBudgetById() {
        Long id = 1L;
        Budget budget = new Budget();
        budget.setId(id);
        budget.setName("Groceries");
        budget.setAmount(new BigDecimal("500.00"));
        budget.setId(1L);

        when(budgetRepository.findById(id)).thenReturn(java.util.Optional.of(budget));

        Budget result = budgetService.findBudgetById(id);
        assertEquals(budget, result);
        verify(budgetRepository, times(1)).findById(id);
    }

    @Test
    void testFindBudgetByIdNotFound() {
        Long id = 1L;
        when(budgetRepository.findById(id)).thenReturn(java.util.Optional.empty());

        Budget result = budgetService.findBudgetById(id);
        assertEquals(null, result);
        verify(budgetRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteBudget() {
        Long id = 1L;
        budgetService.deleteBudget(id);
        verify(budgetRepository, times(1)).deleteById(id);
    }
    
}
