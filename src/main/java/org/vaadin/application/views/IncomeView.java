package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.vaadin.application.MainLayout;
import org.vaadin.application.model.Income;
import org.vaadin.application.model.Note;
import org.vaadin.application.service.IncomeService;
import org.vaadin.application.service.NoteService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

/**
 * The IncomeView class provides the user interface for managing income entries within the application.
 * Users can add, update, view, and delete income records. The class also provides a dashboard to display 
 * the total forecasted income for the month, a breakdown of income sources, and allows users to add notes about their income.
 * 
 * <p>This class extends {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize
 * the components vertically on the page. It uses various Vaadin components like {@link com.vaadin.flow.component.grid.Grid},
 * {@link com.vaadin.flow.component.textfield.TextField}, {@link com.vaadin.flow.component.datepicker.DatePicker}, 
 * and {@link com.vaadin.flow.component.combobox.ComboBox} to create an interactive user interface.</p>
 * 
 * <p>The {@code @Route} annotation maps this view to the "income" URL path and associates 
 * it with the {@link org.vaadin.application.MainLayout}.</p>
 * 
 * <p>This class interacts with the following services: {@link org.vaadin.application.service.IncomeService}
 * for managing income data, {@link org.vaadin.application.service.NoteService} for managing notes,
 * {@link org.vaadin.application.service.SessionService} for managing session-related data, and 
 * {@link org.vaadin.application.service.UserService} for retrieving user information.</p>
 * 
 * @see org.vaadin.application.service.IncomeService
 * @see org.vaadin.application.service.NoteService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "income", layout = MainLayout.class)
public class IncomeView extends VerticalLayout {

    private final Grid<Income> grid = new Grid<>(Income.class);
    private final TextField sourceField = new TextField("Source");
    private final TextField amountField = new TextField("Amount");
    private final DatePicker datePicker = new DatePicker("Date");
    private final ComboBox<String> frequencyField = new ComboBox<>("Payment Frequency");

    private final IncomeService incomeService;
    private final NoteService noteService;
    private final SessionService sessionService;
    private final UserService userService;

    private H2 cardValue;
    private Div totalIncomeCard;
    private Div incomeSourcesCard;
    private Div notesSection;

    private Income selectedIncome; 

    /**
     * Constructs a new IncomeView and initializes the components and layout.
     * 
     * @param incomeService the service used to manage income data
     * @param noteService the service used to manage notes
     * @param sessionService the service used to manage session-related data, particularly the logged-in user
     * @param userService the service used to manage user data
     */
    public IncomeView(
            IncomeService incomeService,
            NoteService noteService,
            SessionService sessionService,
            UserService userService) {
        this.incomeService = incomeService;
        this.noteService = noteService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();
        configureForm();

        Button addButton = new Button("Add/Update Income", event -> addOrUpdateIncome());
        Button deleteButton = new Button("Delete Income", event -> deleteIncome());

        HorizontalLayout formLayout =
                new HorizontalLayout(
                        sourceField, amountField, datePicker, frequencyField, addButton, deleteButton);
        formLayout.setWidthFull();
        formLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        
        cardValue = new H2("$ 0.00");
        totalIncomeCard = createDashboardCard("Forecasted Total Income for this Month", cardValue);

        notesSection = new Div();
        notesSection.addClassName("notes-section");

        TextField notesInput = new TextField();
        notesInput.setPlaceholder("Add a note and press Enter");
        notesInput.setWidthFull();

        notesInput.addKeyPressListener(
                event -> {
                    String note = notesInput.getValue();
                    if (!note.trim().isEmpty()) {
                        addNoteToDatabase(note);
                        addNoteToCard(null, note);
                        notesInput.clear();
                    }
                });

        VerticalLayout notesLayout = new VerticalLayout();
        notesLayout.setWidthFull();
        notesLayout.add(new H3("Notes about Income"), notesSection, notesInput);

        totalIncomeCard.add(notesLayout);

        incomeSourcesCard = createIncomeSourcesCard("Income Sources Breakdown", new Div());

        H1 logo = new H1("Income");

        HorizontalLayout cardsAndGridLayout = new HorizontalLayout(totalIncomeCard, incomeSourcesCard);
        cardsAndGridLayout.setWidthFull();
        cardsAndGridLayout.setFlexGrow(1, totalIncomeCard);
        cardsAndGridLayout.setFlexGrow(1, incomeSourcesCard);
        cardsAndGridLayout.setAlignItems(FlexComponent.Alignment.START);

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(logo, cardsAndGridLayout, formLayout, grid);
        mainLayout.setSpacing(false);

        add(mainLayout);

        listIncomes();
        listNotes();
        updateTotalIncome();
    }

    /**
     * Configures the grid to display the list of incomes, including source, amount, date,
     * and payment frequency. Adds an "Actions" column with an edit button for each income entry.
     */
    private void configureGrid() {
        grid.setColumns("source", "amount", "date", "paymentFrequency");
        grid.addComponentColumn(income -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(event -> editIncome(income));
            return editButton;
        }).setHeader("Actions");
    
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editIncome(event.getValue());
            }
        });


        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                editIncome(event.getValue());
            }
        });
    }

    /**
     * Configures the form fields for adding or updating income entries.
     */
    private void configureForm() {
        sourceField.setPlaceholder("e.g., Salary");
        amountField.setPlaceholder("e.g., 1000.00");
        datePicker.setPlaceholder("Select a date");

        frequencyField.setItems("Weekly", "Biweekly", "Monthly");
        frequencyField.setPlaceholder("Select payment frequency");
    }

    /**
     * Lists the income entries for the currently logged-in user.
     */
    private void listIncomes() {
        Long userId = sessionService.getLoggedInUserId();
        List<Income> incomes = incomeService.getIncomesByUserId(userId);
        grid.setItems(incomes);
    }

    /**
     * Adds or updates an income entry based on the user input. If the user input is invalid, 
     * a notification will be displayed.
     */
    private void addOrUpdateIncome() {
        String source = sourceField.getValue();
        String amountText = amountField.getValue();
        LocalDate date = datePicker.getValue();
        String paymentFrequency = frequencyField.getValue();
    
        if (source.trim().isEmpty() || amountText.trim().isEmpty() || date == null || paymentFrequency == null) {
            Notification.show("Please fill in all fields: Source, Amount, Date, and Payment Frequency.");
            return;
        }
    
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                Notification.show("Amount must be greater than 0.");
                return;
            }
        } catch (NumberFormatException e) {
            Notification.show("Amount must be a valid number.");
            return;
        }
    
        if (selectedIncome == null) {
            addIncome(source, amount, date, paymentFrequency);
        } else {
            updateIncome(source, amount, date, paymentFrequency);
        }
    }
    
    /**
     * Adds a new income to the database and updates the grid to display the new income entry.
     * 
     * @param source the source of the income
     * @param amount the amount of the income
     * @param date the date of the income
     * @param paymentFrequency the payment frequency of the income
     */
    private void addIncome(String source, BigDecimal amount, LocalDate date, String paymentFrequency) {
        Income income = new Income();
        income.setSource(source);
        income.setAmount(amount);
        income.setDate(java.sql.Date.valueOf(date)); // Convert LocalDate to java.sql.Date
        income.setPaymentFrequency(paymentFrequency);
        income.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
    
        incomeService.addIncome(income);
        Notification.show("Income added successfully");
        listIncomes();
        updateTotalIncome();
        clearForm();
    }
    
    /**
     * Updates the details of an existing income in the database and updates the grid
     * to display the updated income entry.
     * 
     * @param source the updated source of the income
     * @param amount the updated amount of the income
     * @param date the updated date of the income
     * @param paymentFrequency the updated payment frequency of the income
     */

    private void updateIncome(String source, BigDecimal amount, LocalDate date, String paymentFrequency) {
        if (selectedIncome != null) {
            selectedIncome.setSource(source);
            selectedIncome.setAmount(amount);
            selectedIncome.setDate(java.sql.Date.valueOf(date));
            selectedIncome.setPaymentFrequency(paymentFrequency);
    
            incomeService.updateIncome(selectedIncome);
            Notification.show("Income updated successfully");
            listIncomes();
            updateTotalIncome();
            clearForm();
            selectedIncome = null;
        }
    }

    /**
     * Prepares the form with the selected income's details for editing.
     * 
     * @param income the income entry to be edited
     */
    private void editIncome(Income income) {
        selectedIncome = income;

        sourceField.setValue(income.getSource());
        amountField.setValue(income.getAmount().toString());
        datePicker.setValue(((Date) income.getDate()).toLocalDate());
        frequencyField.setValue(income.getPaymentFrequency());
    }

    /**
     * Deletes the selected income from the database and updates the grid. 
     * If no income is selected, a notification is displayed prompting the user to select one.
     */
    private void deleteIncome() {
        Income selectedIncome = grid.asSingleSelect().getValue();
        if (selectedIncome != null) {
            incomeService.deleteIncome(selectedIncome.getId());
            Notification.show("Income deleted successfully");
            listIncomes();
            updateTotalIncome();
        } else {
            Notification.show("Please select an income to delete");
        }
    }

    /**
     * Clears the input fields in the income creation/update form.
     */
    private void clearForm() {
        sourceField.clear();
        amountField.clear();
        datePicker.clear();
        frequencyField.clear();
        selectedIncome = null;
    }

     /**
     * Creates a dashboard card to display a specific title and value, used here to show 
     * the forecasted total income for the current month.
     * 
     * @param title the title of the dashboard card
     * @param valueComponent the value component to be displayed in the dashboard card
     * @return a Div containing the visual representation of the dashboard card
     */
    private Div createDashboardCard(String title, H2 valueComponent) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        H3 cardTitle = new H3(title);
        cardTitle.addClassName("card-title");

        valueComponent.addClassName("card-value");

        card.add(cardTitle, valueComponent);
        return card;
    }

    /**
     * Creates a dashboard card to display a breakdown of income sources. This card is updated
     * as new incomes are added or existing incomes are updated or deleted.
     * 
     * @param title the title of the dashboard card
     * @param content the content component to be displayed in the dashboard card
     * @return a Div containing the visual representation of the income sources breakdown card
     */
    private Div createIncomeSourcesCard(String title, Div content) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        H2 cardTitle = new H2(title);
        cardTitle.addClassName("card-title");

        content.addClassName("card-content");

        card.add(cardTitle, content);
        return card;
    }

    /**
     * Updates the total income displayed on the dashboard by calculating the sum 
     * of all incomes for the currently logged-in user.
     */
    private void updateTotalIncome() {
        Long userId = sessionService.getLoggedInUserId();
        List<Income> incomes = incomeService.getIncomesByUserId(userId);
        BigDecimal totalIncome =
                incomes.stream().map(Income::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        cardValue.setText("$ " + totalIncome.toString());

        updateIncomeSourcesCard(incomes, totalIncome);
    }

     /**
     * Updates the income sources breakdown card by calculating the percentage contribution
     * of each income source to the total income.
     * 
     * @param incomes the list of incomes to be displayed
     * @param totalIncome the total income to be used for calculating percentages
     */
    private void updateIncomeSourcesCard(List<Income> incomes, BigDecimal totalIncome) {
        Map<String, BigDecimal> sourcePercentages =
                incomes.stream()
                        .collect(
                                Collectors.groupingBy(
                                        Income::getSource,
                                        Collectors.reducing(BigDecimal.ZERO, Income::getAmount, BigDecimal::add)));
    
        Div content = new Div();
    
        // Add the header only if it hasn't been added yet
        if (content
                .getElement()
                .getChildren()
                .noneMatch(child -> child.getClass().equals("card-title"))) {
            H2 cardTitle = new H2("Income Stream Overview");
            cardTitle.addClassName("card-title");
            content.add(cardTitle);
        }
    
        sourcePercentages.forEach(
                (source, amount) -> {
                    @SuppressWarnings("deprecation")
                    BigDecimal percentage =
                            amount
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(totalIncome, 2, BigDecimal.ROUND_HALF_UP);
    
                    Div sourceContainer = new Div();
                    sourceContainer.addClassName("source-container");
    
                    Paragraph sourceParagraph = new Paragraph();
                    sourceParagraph.getElement().setText(source);
                    sourceParagraph.getElement().getStyle().set("font-weight", "bold");
                    sourceContainer.add(sourceParagraph);
    
                    String amountText = "$ " + amount.toString();
                    String percentageText = percentage.toString() + "% of Total Income";
                    Paragraph detailsParagraph = new Paragraph(amountText + " (" + percentageText + ")");
                    sourceContainer.add(detailsParagraph);
    
                    content.add(sourceContainer);
                });
    
        incomeSourcesCard.removeAll();
        incomeSourcesCard.add(content);
    }
    
    /**
     * Fetches and lists the notes related to income for the currently logged-in user.
     */
    private void listNotes() {
        Long userId = sessionService.getLoggedInUserId();
        List<Note> notes = noteService.getNotesByUserId(userId);

        for (Note note : notes) {
            addNoteToCard(note.getId(), note.getContent());
        }
    }

        /**
     * Adds a note to the notes section of the dashboard and stores it in the database.
     * 
     * @param noteId the ID of the note being added (if available)
     * @param note the content of the note being added
     */

    private void addNoteToCard(Long noteId, String note) {
        HorizontalLayout noteLayout = new HorizontalLayout();
        noteLayout.setWidthFull();
        noteLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    
        Div noteContainer = new Div();
        noteContainer.getStyle().set("width", "100%");
        noteContainer.getStyle().set("overflow-wrap", "break-word");
        noteContainer.getStyle().set("word-wrap", "break-word");
        noteContainer.getStyle().set("hyphens", "auto");
        noteContainer.getStyle().set("margin-bottom", "10px");
    
        Paragraph noteParagraph = new Paragraph("• " + note);
        noteParagraph.getStyle().set("margin", "0");
        noteParagraph.getStyle().set("overflow-wrap", "break-word");
        noteParagraph.getStyle().set("word-wrap", "break-word");
    
        noteContainer.add(noteParagraph);
    
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(
            event -> {
              noteService.deleteNoteById(noteId);
              notesSection.remove(noteLayout);
            });
    
        noteLayout.add(noteContainer, deleteButton);
        notesSection.add(noteLayout);
      }
    
      /**
     * Adds a note to the database.
     * 
     * @param noteContent the content of the note being added
     */
    private void addNoteToDatabase(String noteContent) {
        Note note = new Note();
        note.setContent(noteContent);
        note.setUserId(sessionService.getLoggedInUserId());
        noteService.addNote(note);
    }
}
