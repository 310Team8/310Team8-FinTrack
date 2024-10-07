package org.vaadin.application.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.application.MainLayout;
import org.vaadin.application.model.BusinessProfile;
import org.vaadin.application.model.Employee;
import org.vaadin.application.service.BusinessProfileService;
import org.vaadin.application.service.EmployeeService;
import org.vaadin.application.service.SessionService;
import org.vaadin.application.service.UserService;

/**
 * The BusinessProfileView class provides the user interface for managing business profiles within the application.
 * Users can add, update, view, and delete business profiles, and allows users to fill out business profile details like business name and description.
 * This view also allows a bussiness to manage its employees, including adding and deleting employees, and viewing their details.
 *
 * <p>
 * This class extends {@link com.vaadin.flow.component.orderedlayout.VerticalLayout} to organize the components vertically on the page. It includes a {@link com.vaadin.flow.component.grid.Grid} to display the list of employees, {@link com.vaadin.flow.component.textfield.TextField} for user input, and {@link com.vaadin.flow.component.button.Button} for form actions.
 * </p>
 *
 * <p>
 * The {@code @Route} annotation maps this view to the "business" URL path and associates it with the {@link org.vaadin.application.MainLayout}.
 * </p>
 *
 * <p>
 * This class interacts with the following services: {@link org.vaadin.application.service.BusinessProfileService} for managing business profile data, {@link org.vaadin.application.service.SessionService} for managing session-related data, and {@link org.vaadin.application.service.UserService} for retrieving user information.
 * </p>
 * 
 * @see org.vaadin.application.service.BusinessProfileService
 * @see org.vaadin.application.service.SessionService
 * @see org.vaadin.application.service.UserService
 */
@Route(value = "business", layout = MainLayout.class)
public class BusinessProfileView extends VerticalLayout {
    
    private final Grid<Employee> grid = new Grid<>(Employee.class);
    private final TextField nameField = new TextField("Business Name");
    private final TextField descriptionField = new TextField("Description");
    private final TextField businessType = new TextField("Business Type");
    private final TextField businessAddress = new TextField("Business Address");
    private final TextField businessCity = new TextField("Business City");
    private final TextField phoneNumber = new TextField("Phone Number");
    private final TextField personInCharge = new TextField("Person In Charge");
    private final TextField salaryField = new TextField("Salary");
    private final TextField employeeNameField = new TextField("Employee Name");
    private final TextField employeeRoleField = new TextField("Employee Role");

    private final transient BusinessProfileService businessProfileService;
    private final transient UserService userService;
    private final transient SessionService sessionService;  
    private final transient EmployeeService employeeService;
    
    private Div businessProfileCard;

    /**
     * Constructs a new BusinessProfileView and initializes the components for managing business profiles and employees.
     * This constructor injects the {@link org.vaadin.application.service.BusinessProfileService}, {@link org.vaadin.application.service.SessionService}, {@link org.vaadin.application.service.UserService}, and {@link org.vaadin.application.service.EmployeeService} services.
     * The view includes a grid for displaying employees, text fields for entering business profile and employee details, and buttons for adding, updating, and deleting business profiles and employees.
     *
     * @param businessProfileService the service for managing business profile data
     * @param sessionService the service for managing session-related data
     * @param userService the service for retrieving user information
     * @param employeeService the service for managing employee data
     */
    @Autowired
    public BusinessProfileView(BusinessProfileService businessProfileService, SessionService sessionService, UserService userService, EmployeeService employeeService) {
        this.businessProfileService = businessProfileService;
        this.userService = userService;
        this.sessionService = sessionService;
        this.employeeService = employeeService;

        configureGrid();

        // Set placeholders for text fields
        salaryField.setPlaceholder("e.g. 50000.00");
        employeeNameField.setPlaceholder("e.g. John Doe");
        employeeRoleField.setPlaceholder("e.g. Manager");
        nameField.setPlaceholder("e.g. John's Bakery");
        descriptionField.setPlaceholder("e.g. A bakery that specializes in cakes and pastries");
        businessType.setPlaceholder("e.g. Bakery");
        businessAddress.setPlaceholder("e.g. 123 Main St");
        businessCity.setPlaceholder("e.g. New York");
        phoneNumber.setPlaceholder("e.g. 123-456-7890");
        personInCharge.setPlaceholder("e.g. John Doe");

        Button addEmployeeButton = new Button("Add Employee", event -> addEmployee());
        Button saveBusinessProfileButton = new Button("Save", event -> saveBusinessProfile());
        Button clearBusinessProfileButton = new Button("Clear", event -> clearBusinessProfile());

        // Create a layout for the employee form
        HorizontalLayout EmployeeLayout = new HorizontalLayout(employeeNameField, employeeRoleField, salaryField, addEmployeeButton);
        EmployeeLayout.setWidthFull();
        EmployeeLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        // Create a card for the business profile form
        businessProfileCard = createBusinessProfileCard(nameField, descriptionField, businessType, businessAddress, businessCity, phoneNumber, personInCharge, saveBusinessProfileButton, clearBusinessProfileButton);

        H1 logo = new H1("Business Profile");

        // Create the main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(logo, businessProfileCard, EmployeeLayout, grid);
        mainLayout.setSpacing(false);

        add(mainLayout);

        listEmployees();
        if (businessProfileService.getBusinessProfilesByUserId(sessionService.getLoggedInUserId()).isEmpty()) {
            Notification.show("Please fill in your business profile");
        } else {
            listBusinessProfiles();
        }   
    }

    /**
     * Creates a card layout for the business profile form, which includes text fields for entering business profile details and buttons for saving and clearing the form.
     *
     * @param nameField the text field for entering the business name
     * @param descriptionField the text field for entering the business description
     * @param businessType the text field for entering the business type
     * @param businessAddress the text field for entering the business address
     * @param businessCity the text field for entering the business city
     * @param phoneNumber the text field for entering the business phone number
     * @param personInCharge the text field for entering the person in charge of the business
     * @param saveButton the button for saving the business profile
     * @param clearButton the button for clearing the business profile form
     * @return a Div element containing the business profile form
     */
    private Div createBusinessProfileCard(TextField nameField, TextField descriptionField, TextField businessType, TextField businessAddress, TextField businessCity, TextField phoneNumber, TextField personInCharge, Button saveButton, Button clearButton) {
        Div card = new Div();
        card.addClassName("dashboard-card");
        card.setWidthFull();

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, clearButton);
        HorizontalLayout row1 = new HorizontalLayout(nameField, descriptionField);
        HorizontalLayout row2 = new HorizontalLayout(businessType, businessAddress);
        HorizontalLayout row3 = new HorizontalLayout(businessCity, phoneNumber);
        HorizontalLayout row4 = new HorizontalLayout(personInCharge);

        // Set rows to full width
        row1.setWidthFull();
        row2.setWidthFull();
        row3.setWidthFull();
        // set row 4 to half width
        row4.setWidth("50%");

        // Make components in each row take their space proportionally
        row1.setFlexGrow(1, nameField, descriptionField);
        row2.setFlexGrow(1, businessType, businessAddress);
        row3.setFlexGrow(1, businessCity, phoneNumber);
        row4.setFlexGrow(1, personInCharge);

        card.add(buttonLayout, row1, row2, row3, row4);
        return card;
    }

    /**
     * Configures the grid for displaying employees, including the columns to display and the action buttons for each row.
     */
    private void configureGrid() {
        grid.setColumns("employeeName", "employeeRole", "salary");

        grid.addComponentColumn(employee -> {
            Button editButton = new Button("delete");
            editButton.addClickListener(event -> 
                deleteEmployee(employee));
            return editButton;
        }).setHeader("Actions");
    }

    /**
     * Deletes an employee from the database and updates the grid to reflect the changes.
     *
     * @param employee the employee to delete
     */
    private void deleteEmployee(Employee employee) {
        employeeService.deleteEmployee(employee.getId());
        Notification.show("Employee deleted successfully");
        listEmployees(); 
    }

    /**
     * Adds an employee to the database and updates the grid to reflect the changes.
     */
    private void addEmployee() {
        String name = employeeNameField.getValue();
        String role = employeeRoleField.getValue();
        BigDecimal salary = new BigDecimal(salaryField.getValue());

        if (name.isEmpty() || role.isEmpty() || salary == null) {
            Notification.show("Please enter all information");
            return;
        }

        Employee employee = new Employee();
        employee.setEmployeeName(name);
        employee.setEmployeeRole(role);
        employee.setSalary(salary);
        employee.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
        employeeService.addEmployee(employee);
        Notification.show("Employee added successfully");
        listEmployees();
    }

    /**
     * Lists the employees associated with the currently logged-in user.
     */
    private void listEmployees() {
        Long userId = sessionService.getLoggedInUserId();
        List<Employee> employees = employeeService.getEmployeesByUserId(userId);
        grid.setItems(employees);
    }

    /**
     * Lists the business profile associated with the currently logged-in user.
     */
    private void listBusinessProfiles() {
        Long userId = sessionService.getLoggedInUserId();
        BusinessProfile businessProfile = businessProfileService.getLastBusinessProfileByUserId(userId);
        nameField.setValue(businessProfile.getBusinessName());
        descriptionField.setValue(businessProfile.getBusinessDescription());
        businessType.setValue(businessProfile.getBusinessType());
        businessAddress.setValue(businessProfile.getBusinessAddress());
        businessCity.setValue(businessProfile.getBusinessCity());
        phoneNumber.setValue(businessProfile.getPhoneNumber());
        personInCharge.setValue(businessProfile.getPersonInCharge());
    }

    /**
     * Saves the business profile to the database and updates the grid to reflect the changes.
     */
    private void saveBusinessProfile() {
        String name = nameField.getValue();
        String description = descriptionField.getValue();
        String type = businessType.getValue();
        String address = businessAddress.getValue();
        String city = businessCity.getValue();
        String phone = phoneNumber.getValue();
        String person = personInCharge.getValue();
        System.out.println("name: " + name);

        if (name.isEmpty() || description.isEmpty() || type.isEmpty() || address.isEmpty() || city.isEmpty() || phone.isEmpty() || person.isEmpty()) {
            Notification.show("Please enter all information");
            return;
        }

        BusinessProfile businessProfile = new BusinessProfile();
        businessProfile.setBusinessName(name);
        businessProfile.setBusinessDescription(description);
        businessProfile.setBusinessType(type);
        businessProfile.setBusinessAddress(address);
        businessProfile.setBusinessCity(city);
        businessProfile.setPhoneNumber(phone);
        businessProfile.setPersonInCharge(person);
        businessProfile.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
        businessProfileService.addBusinessProfile(businessProfile);
        Notification.show("Business profile saved successfully");
        System.out.println("success");
        listBusinessProfiles();
    }

    /**
     * Clears the business profile form.
     */
    private void clearBusinessProfile() {
        nameField.clear();
        descriptionField.clear();
        businessType.clear();
        businessAddress.clear();
        businessCity.clear();
        phoneNumber.clear();
        personInCharge.clear();

        Notification.show("Business profile cleared");
    }
}
