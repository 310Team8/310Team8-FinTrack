package org.vaadin.example.views;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.vaadin.example.service.ExpenseCategoryService;
import org.vaadin.example.service.ExpenseService;
import org.vaadin.example.service.IncomeService;
import org.vaadin.example.model.ExpenseCategory;
import java.util.List;
import java.util.stream.Collectors;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;

class DashboardViewTests {

    @Mock
    private ExpenseService expenseService;

    @Mock
    private IncomeService incomeService;

    @Mock
    private ExpenseCategoryService expenseCategoryService;

    private DashboardView dashboardView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        VaadinSession vaadinSession = Mockito.mock(VaadinSession.class);
        VaadinSession.setCurrent(vaadinSession);
        Mockito.when(vaadinSession.getAttribute("userId")).thenReturn(1L);

        dashboardView = new DashboardView(expenseService, incomeService, expenseCategoryService);
    }

    @ParameterizedTest
    @CsvSource({
        "'Total Expenses for this Month', '1000'",
        "'Total Income for this Month', '2000'",
        "'', '0'",
        "'', ''",
        "'Total Expenses for this Month', ''"
    })
    void testCreateDashboardCard(String title, String value) {
        
        Div card = dashboardView.createDashboardCard(title, value);

        assertEquals("dashboard-card", card.getClassName());

        H2 cardTitle = (H2) card.getChildren().filter(c -> c instanceof H2 && ((H2) c).getClassName().equals("card-title")).findFirst().get();
        assertEquals(title, cardTitle.getText());

        H2 cardValue = (H2) card.getChildren().filter(c -> c instanceof H2 && ((H2) c).getClassName().equals("card-value")).findFirst().get();
        assertEquals("$" + value, cardValue.getText());
    }

    @Test
    void testCreateExpenseCategoryList() {
        
        ExpenseCategory expenseCategory = new ExpenseCategory();
        String item1name = "Rent";
        expenseCategory.setName(item1name);
        ExpenseCategory expenseCategory2 = new ExpenseCategory();
        String item2name = "Groceries";
        expenseCategory2.setName(item2name);
        ExpenseCategory expenseCategory3 = new ExpenseCategory();
        String item3name = "Utilities";
        expenseCategory3.setName(item3name);
        
        List<ExpenseCategory> expenseCategories = List.of(
            expenseCategory,
            expenseCategory2,
            expenseCategory3
            );
            Mockito.when(expenseCategoryService.getExpenseCategoriesByUserId(1L)).thenReturn(expenseCategories);
            
        VerticalLayout expenseCategoryList = dashboardView.createExpenseCategoryList(1L);
        assertNotNull(expenseCategoryList);

        H2 cardTitle = (H2) expenseCategoryList.getChildren().filter(c -> c instanceof H2 && ((H2) c).getClassName().equals("category-title")).findFirst().get();
        assertEquals("Expense Categories", cardTitle.getText());
        
        List<Div> categoryItems = expenseCategoryList.getChildren().filter(c -> c instanceof Div && ((Div) c).getClassName().equals("category-name"))
        .map(c -> (Div) c).collect(Collectors.toList());

        assertEquals(3, categoryItems.size());

        List<String> itemNames= List.of(item1name, item2name, item3name);
        for (int i = 0; i < categoryItems.size(); i++) {
            Div categoryItem = categoryItems.get(i);
            assertEquals("category-item", categoryItem.getClassName());
            assertEquals(itemNames.get(i), categoryItem.getText());

            H2 categoryName = (H2) categoryItem.getChildren().filter(c -> c instanceof H2 && ((H2) c).getClassName().equals("category-name")).findFirst().get();
            assertEquals(expenseCategories.get(i).getName(), categoryName.getText());
        }

        assertEquals("category-layout", expenseCategoryList.getClassName());
    }
}
