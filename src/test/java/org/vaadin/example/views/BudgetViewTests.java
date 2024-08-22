package org.vaadin.example.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.model.User;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.BudgetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import com.vaadin.flow.component.notification.Notification;

import java.math.BigDecimal;
import java.util.List;

class BudgetViewTests {

    @Mock
    private BudgetService budgetService;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BudgetView budgetView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListBudgets() {
        Long userId = 1L;
        Budget budget = new Budget();
        budget.setName("name");
        budget.setAmount(new BigDecimal("5000"));
        when(sessionService.getLoggedInUserId()).thenReturn(userId);
        when(budgetService.getBudgetsByUserId(userId)).thenReturn(List.of(budget));

        budgetView.listBudgets();

        Mockito.verify(budgetService).getBudgetsByUserId(userId);
    }

    @ParameterizedTest
    @CsvSource({
        "'name', '5000'",
        "'name', ''",
        "'', 5000",
        "'', ''"
    })
    void testClearForm(String name, String amount) {
        budgetView.nameField.setValue(name);
        budgetView.amountField.setValue(amount);

        budgetView.clearForm();

        assertEquals("", budgetView.nameField.getValue());
        assertEquals("", budgetView.amountField.getValue());
    }

    @Test
    void testAddBudgetValidAmount() {
        String name = "name";
        String amountText = "5000";
        BigDecimal amount = new BigDecimal(amountText);
        
        try (var notification = Mockito.mockStatic(Notification.class)) {
            
            budgetView.nameField.setValue(name);
            budgetView.amountField.setValue(amountText);
    
            when(sessionService.getLoggedInUserId()).thenReturn(1L);
            when(userService.findUserById(1L)).thenReturn(new User());
    
            budgetView.addBudget();
    
            Mockito.verify(budgetService).addBudget(any(Budget.class));
            assertEquals("", budgetView.nameField.getValue());
            assertEquals("", budgetView.amountField.getValue());
            notification.verify(() -> Notification.show("Budget added successfully", 3000, Notification.Position.TOP_CENTER));
        }
    }

    @Test
    void testAddBudgetInvalidAmount() {
        String name = "name";
        String amountText = "invalid";
        
        try (var notification = Mockito.mockStatic(Notification.class)) {
            
            budgetView.nameField.setValue(name);
            budgetView.amountField.setValue(amountText);
    
            budgetView.addBudget();
    
            assertThrows(NumberFormatException.class, () -> new BigDecimal(amountText));
            Mockito.verify(budgetService, never()).addBudget(any(Budget.class));
            notification.verify(() -> Notification.show("Please enter a valid amount", 3000, Notification.Position.TOP_CENTER));
        }
    }

    @Test
    void testDeleteBudgetWhenBudgetSelected() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setName("name");
        budget.setAmount(new BigDecimal("5000"));

        try (var notification = Mockito.mockStatic(Notification.class)) {
            budgetView.deleteBudget(budget);
    
            Mockito.verify(budgetService).deleteBudget(1L);
            notification.verify(() -> Notification.show("Budget deleted successfully", 3000, Notification.Position.TOP_CENTER));
        }
    }
    
}
