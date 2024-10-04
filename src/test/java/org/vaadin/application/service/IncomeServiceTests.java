package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.application.model.Expense;
import org.vaadin.application.model.Income;
import org.vaadin.application.model.User;
import org.vaadin.application.repository.IncomeRepository;
import org.vaadin.application.service.IncomeService;

import java.util.Arrays;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

public class IncomeServiceTests {

    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeService incomeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIncomesByUserId() {
        User user = new User();
        Income income1 = new Income();
        income1.setId(1L);
        income1.setUser(user);
        Income income2 = new Income();
        income2.setId(2L);
        income2.setUser(user);

        when(incomeRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(income1, income2));

        List<Income> result = incomeService.getIncomesByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals(income1, result.get(0));
        assertEquals(income2, result.get(1));
    }

    @Test
    void testAddIncome() {
        Income income = new Income();
        income.setId(1L);

        when(incomeRepository.save(income)).thenReturn(income);

        Income result = incomeService.addIncome(income);

        assertEquals(income, result);
        verify(incomeRepository, times(1)).save(income);
    }

    @Test
    void testFindIncomeById() {
        Income income = new Income();
        income.setId(1L);

        when(incomeRepository.findById(income.getId())).thenReturn(java.util.Optional.of(income));

        Income result = incomeService.findIncomeById(income.getId());

        assertEquals(income, result);
        verify(incomeRepository, times(1)).findById(income.getId());
    }

    @Test
    void testFindIncomeByIdNotFound() {
        Long id = 1L;

        when(incomeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        Income result = incomeService.findIncomeById(id);

        assertEquals(null, result);
        verify(incomeRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteIncome() {
        Long id = 1L;

        doNothing().when(incomeRepository).deleteById(id);

        incomeService.deleteIncome(id);

        verify(incomeRepository, times(1)).deleteById(id);
    }

    @Test
    void getTotalIncomeForCurrentMonth() {
        Long userId = 1L;
        BigDecimal totalIncome = new BigDecimal("100.00");

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        when(incomeRepository.findTotalIncomeForPeriod(userId, startOfMonth, endOfMonth)).thenReturn(totalIncome);

        BigDecimal result = incomeService.getTotalIncomeForCurrentMonth(userId);

        assertEquals(totalIncome, result);
        verify(incomeRepository, times(1)).findTotalIncomeForPeriod(userId, startOfMonth, endOfMonth);
    }
}