package org.vaadin.example.service;

import org.vaadin.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.FinancialGoal;
import org.vaadin.example.repository.FinancialGoalRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FinancialGoalServiceTests {
 
    @Mock
    private FinancialGoalRepository financialGoalRepository;

    @InjectMocks
    private FinancialGoalService financialGoalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFinancialGoalsByUserId() {
        User user = new User();
        user.setId(1L);
        
        FinancialGoal financialGoal1 = new FinancialGoal();
        financialGoal1.setId(1L);
        financialGoal1.setDescription("Buy a new car");
        financialGoal1.setTargetAmount(new BigDecimal("25000.00"));
        financialGoal1.setUser(user);

        FinancialGoal financialGoal2 = new FinancialGoal();
        financialGoal2.setId(2L);
        financialGoal2.setDescription("Take a vacation");
        financialGoal2.setTargetAmount(new BigDecimal("5000.00"));
        financialGoal2.setUser(user);

        List<FinancialGoal> financialGoals = Arrays.asList(financialGoal1, financialGoal2);
        
        when(financialGoalRepository.findByUserId(user.getId())).thenReturn(financialGoals);

        List<FinancialGoal> result = financialGoalService.getFinancialGoalsByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals(financialGoal1, result.get(0));
        assertEquals(financialGoal2, result.get(1)); 
        verify(financialGoalRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testAddFinancialGoal() {
        User user = new User();
        user.setId(1L);

        FinancialGoal financialGoal = new FinancialGoal();
        financialGoal.setId(1L);
        financialGoal.setDescription("Buy a new car");
        financialGoal.setTargetAmount(new BigDecimal("25000.00"));
        financialGoal.setUser(user);

        when(financialGoalRepository.save(financialGoal)).thenReturn(financialGoal);

        FinancialGoal result = financialGoalService.addFinancialGoal(financialGoal);

        assertEquals(financialGoal, result);
        verify(financialGoalRepository, times(1)).save(financialGoal);
    }

    @Test
    void testFindFinancialGoalById() {
        Long id = 1L;
        User user = new User();
        user.setId(1L);

        FinancialGoal financialGoal = new FinancialGoal();
        financialGoal.setId(id);
        financialGoal.setDescription("Buy a new car");
        financialGoal.setTargetAmount(new BigDecimal("25000.00"));
        financialGoal.setUser(user);

        when(financialGoalRepository.findById(id)).thenReturn(java.util.Optional.of(financialGoal));

        FinancialGoal result = financialGoalService.findFinancialGoalById(id);

        assertEquals(financialGoal, result);
        verify(financialGoalRepository, times(1)).findById(id);
    }

    @Test 
    void testFindFinancialGoalByIdNotFound() {
        Long id = 1L;
        when(financialGoalRepository.findById(id)).thenReturn(Optional.empty());
        FinancialGoal result = financialGoalService.findFinancialGoalById(id);
        assertNull(result);
        verify(financialGoalRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteFinancialGoal() {
        Long id = 1L;
        doNothing().when(financialGoalRepository).deleteById(id);
        financialGoalService.deleteFinancialGoal(id);
        verify(financialGoalRepository, times(1)).deleteById(id);
    }
    
}

