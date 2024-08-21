package org.vaadin.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.Expense;
import org.vaadin.example.model.ExpenseCategory;
import org.vaadin.example.model.User;
import org.vaadin.example.repository.ExpenseCategoryRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

public class ExpenseCategoryServiceTests {

    @Mock
    private ExpenseCategoryRepository expenseCategoryRepository;

    @InjectMocks
    private ExpenseCategoryService expenseCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExpenseCategoriesByUserId() {
        User user = new User();
        user.setId(1L);
        
        ExpenseCategory expenseCategory1 = new ExpenseCategory();
        expenseCategory1.setId(1L);
        expenseCategory1.setName("Groceries");
        expenseCategory1.setUser(user);

        ExpenseCategory expenseCategory2 = new ExpenseCategory();
        expenseCategory2.setId(2L);
        expenseCategory2.setName("Utilities");
        expenseCategory2.setUser(user);

        List<ExpenseCategory> expenseCategories = Arrays.asList(expenseCategory1, expenseCategory2);
        
        when(expenseCategoryRepository.findByUserId(user.getId())).thenReturn(expenseCategories);

        List<ExpenseCategory> result = expenseCategoryService.getExpenseCategoriesByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals(expenseCategory1, result.get(0));
        assertEquals(expenseCategory2, result.get(1)); 
    }

    @Test
    void testAddExpenseCategory() {
        User user = new User();
        user.setId(1L);

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        expenseCategory.setName("Groceries");
        expenseCategory.setUser(user);

        when(expenseCategoryRepository.save(expenseCategory)).thenReturn(expenseCategory);

        ExpenseCategory result = expenseCategoryService.addExpenseCategory(expenseCategory);

        assertEquals(expenseCategory, result);
    }

    @Test
    void testFindExpenseCategoryById() {
        User user = new User();
        user.setId(1L);

        ExpenseCategory expenseCategory = new ExpenseCategory();
        expenseCategory.setId(1L);
        expenseCategory.setName("Groceries");
        expenseCategory.setUser(user);

        when(expenseCategoryRepository.findById(expenseCategory.getId())).thenReturn(java.util.Optional.of(expenseCategory));

        ExpenseCategory result = expenseCategoryService.findExpenseCategoryById(expenseCategory.getId());

        assertEquals(expenseCategory, result);
    }

    @Test
    void testFindExpenseCategoryByIdNotFound() {
        Long id = 1L;

        when(expenseCategoryRepository.findById(id)).thenReturn(java.util.Optional.empty());

        ExpenseCategory result = expenseCategoryService.findExpenseCategoryById(id);

        assertEquals(null, result);
    }

    @Test
    void testDeleteExpenseCategory() {
        Long id = 1L;
        doNothing().when(expenseCategoryRepository).deleteById(id);
        expenseCategoryService.deleteExpenseCategory(id);
        verify(expenseCategoryRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetCategoriesByUserId() {
        User user = new User();
        user.setId(1L);
        
        ExpenseCategory expenseCategory1 = new ExpenseCategory();
        expenseCategory1.setId(1L);
        expenseCategory1.setName("Groceries");
        expenseCategory1.setUser(user);

        ExpenseCategory expenseCategory2 = new ExpenseCategory();
        expenseCategory2.setId(2L);
        expenseCategory2.setName("Utilities");
        expenseCategory2.setUser(user);

        List<ExpenseCategory> expenseCategories = Arrays.asList(expenseCategory1, expenseCategory2);
        
        when(expenseCategoryRepository.findByUserId(user.getId())).thenReturn(expenseCategories);

        List<ExpenseCategory> result = expenseCategoryService.getCategoriesByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals(expenseCategory1, result.get(0));
        assertEquals(expenseCategory2, result.get(1)); 
    }
}
