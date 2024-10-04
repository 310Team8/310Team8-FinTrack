package org.vaadin.application.views;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import org.vaadin.addons.chartjs.ChartJs;
import org.vaadin.addons.chartjs.config.BarChartConfig;
import org.vaadin.addons.chartjs.config.LineChartConfig;
import org.vaadin.addons.chartjs.data.BarDataset;
import org.vaadin.addons.chartjs.data.Dataset;
import org.vaadin.addons.chartjs.data.LineDataset;
import org.vaadin.addons.chartjs.options.Position;
import org.vaadin.addons.chartjs.options.scale.Axis;
import org.vaadin.addons.chartjs.options.scale.CategoryScale;
import org.vaadin.addons.chartjs.options.scale.LinearScale;
import org.vaadin.application.MainLayout;
import org.vaadin.application.service.ExpenseService;
import org.vaadin.application.service.IncomeService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

@Route(value = "netCashflowForecast", layout = MainLayout.class)
public class NetCashflowForecastView extends VerticalLayout {
    private final transient  ExpenseService expenseService;
    private final transient  SessionService sessionService;
    private final transient  IncomeService incomeService;
    private ChartJs expenseChart;
    private BarChartConfig expenseConfig;
    private ChartJs cashflowChart;
    private LineChartConfig cashflowConfig;
    private int previousMonths = 3;
    private Map<Month, BigDecimal> predictedCashflows;
    private String[] labels;
    private String rgbaFullOpcity;
    private String rgbaLowOpacity;

  /**
   * Constructs a new NetCashflowForecastView and initializes the components and layout.
   *
   * @param expenseService the service used to manage expense data
   * @param sessionService the service used to manage session-related data, particularly the
   *     logged-in user
   * @param userService the service used to manage user data
   * @param incomeService the service used to manage income data
   */
  public NetCashflowForecastView(
      ExpenseService expenseService, SessionService sessionService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.sessionService = sessionService;
        this.incomeService = incomeService;
        this.predictedCashflows = new LinkedHashMap<>();

        configureLabels();
        configureExpenseChart();
        configureCashflowChart();
        updateExpenseChart();
        updateCashflowChart();

        Div expenseContainer = createChartContainer(expenseChart);
        Div cashflowContainer = createChartContainer(cashflowChart);

        HorizontalLayout chartLayout = new HorizontalLayout();
        chartLayout.add(expenseContainer, cashflowContainer);

        chartLayout.setWidth("100%");

    // Create a ComboBox for selecting months
    ComboBox<Integer> monthsComboBox =
        new ComboBox<>(
            "Select Number of Previous Months for Expense Prediction", Arrays.asList(3, 6, 12));
        monthsComboBox.setValue(previousMonths);
        monthsComboBox.addValueChangeListener(e -> {
          previousMonths = e.getValue();
          updateExpenseChart();
          updateCashflowChart();
        });

        add(new H1("Net Cashflow Forecast"), monthsComboBox);
        add(chartLayout);
    }


    /**
     * Creates a custom Div that holds chart objects
     * @param chart chart that will be put in the div
     * @return custom Div
     */
   private Div createChartContainer(ChartJs chart)  {
    Div chartContainer = new Div();
    chartContainer.setClassName("chart-container");

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(chart);
    horizontalLayout.setHeight("100%");
    horizontalLayout.setWidth("100%");

    chartContainer.add(horizontalLayout);

    return chartContainer;
   }

  /**
   * Configures the expense chart so that data can be added later on.
   */
  private void configureExpenseChart() {
    // Initialize the chart with an empty dataset
    expenseConfig =
        (BarChartConfig)
            new BarChartConfig()
                .data()
                .labels(new String[] {}) 
                .addDataset(
                    new BarDataset()
                        .type()
                        .label("Expenses")
                        .data(new Double[] {})
                    )
                .and()
                .options()
                .responsive(true)
                .maintainAspectRatio(false)
                .title()
                .display(true)
                .position(Position.TOP)
                .text("Predicted Expenses for the Next 12 Months")
                .and()
                .scales() // Add Y-axis configuration
                .add(
                    Axis.Y,
                    new LinearScale()
                        .ticks()
                        .beginAtZero(true)
                        .and()
                        .stacked(true) // Set minimum value to 0
                    ).add(Axis.X, new CategoryScale().stacked(true))
                .and()
                .done();

        expenseChart = new ChartJs(expenseConfig);
        expenseChart.setWidth("100%");
    }

  /** Configures the cashflow chart so that data can be added later on. */
  private void configureCashflowChart() {
    // Initialize the chart with an empty dataset
    cashflowConfig =
        (LineChartConfig)
            new LineChartConfig()
                .data()
                .labels(new String[] {}) // Start with no labels
                .addDataset(
                    new LineDataset()
                        .type()
                        .label("Net Cashflow")
                        .fill(false)
                        .lineTension(0.1)
                        .borderWidth(2)
                        .borderCapStyle("butt")
                        .borderJoinStyle("miter")
                        .pointBorderWidth(1)
                        .pointHoverRadius(5)
                        .pointHoverBorderWidth(2)
                        .pointRadius(1)
                        .pointHitRadius(10)
                        .data(new Double[] {}))
                .and()
                .options()
                .responsive(true)
                .maintainAspectRatio(false)
                .title()
                .display(true)
                .position(Position.TOP)
                .text("Predicted Net Cashflow for the Next 12 Months")
                .and()
                .scales()
                .add(Axis.Y, new LinearScale().ticks().and())
                .and()
                .done();

    cashflowChart = new ChartJs(cashflowConfig);
    cashflowChart.setWidth("100%");
  }


  /** Adds or updates the expense chart data */
  private void updateExpenseChart() {
    Long userId = sessionService.getLoggedInUserId();
    expenseConfig.data().clear();

    List<String> budgets = expenseService.getAllDistinctBudgets(userId);
    expenseConfig.data().labels(labels);

    for (int i = 0; i < budgets.size(); i++) {
        Map<Month, BigDecimal> predictedBudgetExpenses = predictMonthlyExpensesBudget(previousMonths, budgets.get(i)); 
        if (predictedBudgetExpenses != null && !predictedBudgetExpenses.isEmpty()) {
          expenseConfig.data().addDataset(createBarDataset(predictedBudgetExpenses, budgets.get(i)));
        }
    }

    // Expenses with no budgets
    Map<Month, BigDecimal> predictedNoBudgetExpenses =
        predictMonthlyExpensesBudget(previousMonths, "no-budget");
    if (predictedNoBudgetExpenses != null && !predictedNoBudgetExpenses.isEmpty()) {
    expenseConfig.data().addDataset(createBarDataset(predictedNoBudgetExpenses, "No Budget"));
    }    

    expenseChart.update();

  }

  /** Adds or updates the cashflow chart data */
  private void updateCashflowChart() {

    predictNetCashflow();

    Double[] data =
        predictedCashflows.values().stream().map(value -> value != null ? value.setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0).toArray(Double[]::new);

    cashflowConfig.data().labels(labels);

    for (Dataset<?, ?> ds : cashflowConfig.data().getDatasets()) {
      if (ds instanceof LineDataset) {
        LineDataset lds = (LineDataset) ds;
        lds.dataAsList(List.of(data));
        lds.backgroundColor(rgbaLowOpacity);
        lds.borderColor(rgbaFullOpcity);
        lds.pointHoverBackgroundColor(rgbaLowOpacity);
        lds.pointHoverBorderColor(rgbaFullOpcity);
      }
    }

    cashflowChart.update();
  }

  /**
   * Creates a Vaadin Addon Chartjs BarDataset 
   * @param map holds data that will be added to the bar dataset
   * @param label the label for the data
   * @return BarDataset
   */
  private BarDataset createBarDataset(Map<Month, BigDecimal> map, String label) {
    Double[] data =map.values().stream().map(value -> value != null ? value.setScale(2, RoundingMode.HALF_UP).doubleValue() : 0.0).toArray(Double[]::new);

    return new BarDataset()
        .type()
        .label(label)
        .backgroundColor(generateRgbaColour())
        .borderColor("white")
        .borderWidth(2)
        .data(data);
  }

  /**
   * Calculates and predicts the users net cashflow for the next 12 months based
   * on their total monthly incomes and predicted expenses
   */
  private void predictNetCashflow() {
    Long userId = sessionService.getLoggedInUserId();
    BigDecimal totalIncomePerMonth = incomeService.getTotalIncomeAllMonths(userId);
    Map<Month, BigDecimal> predictedTotalExpenses = getTotalPredictedExpenses();
    
    if (totalIncomePerMonth.equals(BigDecimal.ZERO)) {
        for (Map.Entry<Month, BigDecimal> entry : predictedTotalExpenses.entrySet()) {
            predictedTotalExpenses.put(entry.getKey(), BigDecimal.ZERO);
        }
        predictedCashflows = predictedTotalExpenses;
        return;
    }

    for (Map.Entry<Month, BigDecimal> entry : predictedTotalExpenses.entrySet()) {
        BigDecimal predictedNetCashflow = totalIncomePerMonth.subtract(entry.getValue());
        predictedTotalExpenses.put(entry.getKey(), predictedNetCashflow);
    }

    predictedCashflows = predictedTotalExpenses;
}

/**
 * Calculates the total expenses per month (since they are seperated by budget type)
 * @return a map of months and their total expenses
 */
private Map<Month, BigDecimal> getTotalPredictedExpenses() {
    Map<Month, BigDecimal> totalPredictedExpense = new LinkedHashMap<>();
    for (Dataset<?, ?> ds : expenseConfig.data().getDatasets()) {
        if (ds instanceof BarDataset) {
            BarDataset dataset = (BarDataset) ds;
            for (int i = 0; i < 12; i++) {
                Month futureMonth = LocalDate.now().plusMonths(i + (long) 1).getMonth();
                List<Double> data = dataset.getData();
                totalPredictedExpense.put(futureMonth, totalPredictedExpense.getOrDefault(futureMonth, BigDecimal.ZERO).add(BigDecimal.valueOf(data.get(i))));
            }    
        }
    }
    return totalPredictedExpense;
}

  /** Configures an array of labels representing the next 12 months. */
  private void configureLabels() {
    String[] labelsNew = new String[12];
    for (int i = 1; i <= 12; i++) {
      Month futureMonth = LocalDate.now().plusMonths(i).getMonth();
        labelsNew[i - 1] = futureMonth.toString();
    }
    labels = labelsNew;
}

  /**
   * Calculates and predicts the user's expenses for the next 12 months (assuming a linear trend)
   * based on the expenses of the last `previousMonths`.
   *
   * @param previousMonths the number of months that the predicted expenses should be based on
   * @return
   */
  private Map<Month, BigDecimal> predictMonthlyExpensesBudget(int previousMonths, String budgetName) {
    Map<Month, BigDecimal> monthlyExpenses = new EnumMap<>(Month.class);
    Long userId = sessionService.getLoggedInUserId();

    Map<Month, BigDecimal> monthlyPastExpensesBudget =
        expenseService.getExpensesForPreviousMonths(userId, previousMonths, budgetName);


    if (monthlyPastExpensesBudget != null && !monthlyPastExpensesBudget.isEmpty()) {
      
      // Convert the map entries to a list and sort by month
      List<Map.Entry<Month, BigDecimal>> sortedEntries =
          new ArrayList<>(monthlyPastExpensesBudget.entrySet());
      sortedEntries.sort(Comparator.comparing(Map.Entry::getKey));

      List<BigDecimal> expensesList = new ArrayList<>();
      for (Map.Entry<Month, BigDecimal> entry : sortedEntries) {
        expensesList.add(entry.getValue());
      }

      BigDecimal sum = expensesList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal averageExpense = sum.divide(new BigDecimal(expensesList.size()),  RoundingMode.HALF_UP);

      List<BigDecimal> changes = calculateChangesForConsecutiveMonths(expensesList);

      BigDecimal totalChange = changes.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal averageChange = totalChange.divide(changes.isEmpty() ? BigDecimal.ONE : new BigDecimal(changes.size()) , RoundingMode.HALF_EVEN);
     
      for (int i = 1; i <= 12; i++) {
        Month futureMonth = LocalDate.now().plusMonths(i).getMonth();
        BigDecimal predictedMonthExpense = averageExpense.add((averageChange.multiply(new BigDecimal(i))));

        monthlyExpenses.put(futureMonth, predictedMonthExpense.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : predictedMonthExpense);
      }

      return reorderMonths(monthlyExpenses);
    }

    return monthlyExpenses; 
  }


  /**
   * Calculates the change in expenses between months
   *
   * @param expensesList list of expenses ordered by month
   * @return list of expense changes between months
   */
    private List<BigDecimal> calculateChangesForConsecutiveMonths(List<BigDecimal> expensesList) {
        List<BigDecimal> changes = new ArrayList<>();

        Month currentRealLifeMonth = LocalDate.now().getMonth();

        for (int i = 1; i < expensesList.size(); i++) {
            BigDecimal monthExpense = expensesList.get(i);
            BigDecimal previousMonthExpense = expensesList.get(i - 1);

            // Map the previous month (i - 1) to the corresponding month
            Month previousMonth = Month.of(i % 12 + 1);  

            // Skip if both current and previous expenses are zero OR
            // if the previous month corresponds to the real-life current month and we are calculating based off 12 months
            if (previousMonthExpense.compareTo(BigDecimal.ZERO) == 0 && monthExpense.compareTo(BigDecimal.ZERO) == 0 ||
            (previousMonth == currentRealLifeMonth && expensesList.size() == 12)) {
                continue;
            }

            changes.add(monthExpense.subtract(previousMonthExpense));
        }
        
        return changes;
    }


    /**
     * Reorders the months in a map so that they start from the next month of the current date.
     * @param predictedExpenses Map of months and the values associated with that month
     * @return Reordered map
     */
    private Map<Month, BigDecimal> reorderMonths(Map<Month, BigDecimal> predictedExpenses) {
        List<Month> monthsOrder = new ArrayList<>();
        LocalDate startMonth = LocalDate.now().plusMonths(1);
        for (int i = 0; i < 12; i++) {
            monthsOrder.add(startMonth.plusMonths(i).getMonth());
        }

        Map<Month, BigDecimal> reorderedPredictedExpenses = new LinkedHashMap<>();
        for (Month month : monthsOrder) {
            reorderedPredictedExpenses.put(month, predictedExpenses.getOrDefault(month, BigDecimal.ZERO));
        }
        return reorderedPredictedExpenses;
    }

    /**
     * Generates a random rgba colour string
     * @return rgba colour string
     */
    private String generateRgbaColour() {
        SecureRandom random = new SecureRandom();
        int r = random.nextInt(256);  
        int g = random.nextInt(256); 
        int b = random.nextInt(256);
        double a = 0.5;
        double full = 1;
        String format = "rgba(%d,%d,%d,%.1f)";
        rgbaLowOpacity = String.format(format, r, g, b, a);
        rgbaFullOpcity = String.format(format, r, g, b, full);
        return String.format(format, r, g, b, a);
    }
}
