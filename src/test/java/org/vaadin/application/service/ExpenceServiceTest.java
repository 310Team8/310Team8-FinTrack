package org.vaadin.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.application.model.Expense;
import org.vaadin.application.repository.ExpenseRepository;
import org.vaadin.application.service.ExpenseService;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.inject.Inject;

public class ExpenceServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExpensesByUserId() {

        Long userId = 1L;

        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setDescription("Napkins");
        expense1.setAmount(new BigDecimal("25.00"));
        expense1.setDate(Date.valueOf(LocalDate.of(2021, 10, 15)));

        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense1.setDescription("Peaches");
        expense1.setAmount(new BigDecimal("150.00"));
        expense1.setDate(Date.valueOf(LocalDate.of(2024, 1, 13)));

        List<Expense> expenses = Arrays.asList(expense1, expense2);

        when(expenseRepository.findByUserId(userId)).thenReturn(expenses);

        List<Expense> result = expenseService.getExpensesByUserId(userId);
        assertEquals(2, result.size());
        verify(expenseRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testAddExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDescription("Napkins");
        expense.setAmount(new BigDecimal("25.00"));
        expense.setDate(Date.valueOf(LocalDate.of(2021, 10, 15)));

        when(expenseRepository.save(expense)).thenReturn(expense);

        Expense result = expenseService.addExpense(expense);
        assertEquals(1L, result.getId());
        assertEquals("Napkins", result.getDescription());
        assertEquals(new BigDecimal("25.00"), result.getAmount());
        assertEquals(Date.valueOf(LocalDate.of(2021, 10, 15)), result.getDate());
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    void testFindExpenseById() {
        Long id = 1L;
        Expense expense = new Expense();
        expense.setId(id);
        expense.setDescription("Napkins");
        expense.setAmount(new BigDecimal("25.00"));
        expense.setDate(Date.valueOf(LocalDate.of(2021, 10, 15)));

        when(expenseRepository.findById(id)).thenReturn(Optional.of(expense));

        Expense result = expenseService.findExpenseById(id);
        assertEquals(1L, result.getId());
        assertEquals("Napkins", result.getDescription());
        assertEquals(new BigDecimal("25.00"), result.getAmount());
        assertEquals(Date.valueOf(LocalDate.of(2021, 10, 15)), result.getDate());
        verify(expenseRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteExpense() {
        Long id = 1L;
        doNothing().when(expenseRepository).deleteById(id);
        expenseService.deleteExpense(id);
        verify(expenseRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetTotalExpensesForCurrentMonth() {
        Long userId = 1L;
        BigDecimal totalExpenses = new BigDecimal("100.00");

        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        when(expenseRepository.findTotalExpensesForPeriod(userId, startOfMonth, endOfMonth)).thenReturn(totalExpenses);

        BigDecimal result = expenseService.getTotalExpensesForCurrentMonth(userId);
        assertEquals(new BigDecimal("100.00"), result);
        verify(expenseRepository, times(1)).findTotalExpensesForPeriod(userId, startOfMonth, endOfMonth);
    }
}
