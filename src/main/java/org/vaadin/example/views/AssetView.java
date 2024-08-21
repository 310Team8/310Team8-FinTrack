package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Asset;
import org.vaadin.example.service.AssetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "asset", layout = MainLayout.class)
public class AssetView extends VerticalLayout {

    private final FlexLayout assetLayout = new FlexLayout();
    private final TextField nameField = new TextField("Asset Name");
    private final NumberField valueField = new NumberField("Value ($)");
    private final TextField categoryField = new TextField("Category");
    private final NumberField interestRateField = new NumberField("Interest Rate (%)");

    private final AssetService assetService;
    private final SessionService sessionService;
    private final UserService userService;

    private H2 totalAssetsValue;
    private H2 totalChangeValue;
    private Div totalAssetsCard;
    private Div totalChangeCard;

    public AssetView(AssetService assetService, SessionService sessionService, UserService userService) {
        this.assetService = assetService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureLayout();
        listAssets();
        updateTotalAssets();
        updateTotalChangeInAssets();
    }

    private void configureLayout() {
        assetLayout.setWidthFull();
        assetLayout.setFlexWrap(FlexWrap.WRAP);
        assetLayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
        assetLayout.getStyle().set("gap", "20px");

        setAlignItems(Alignment.CENTER);
        setWidthFull();
        setPadding(false);
        setSpacing(false);

        H1 title = new H1("My Assets");
        title.getStyle().set("color", "#333").set("font-weight", "600").set("font-size", "2em");

        configureFormFields();

        Button addButton = new Button("Add Asset", VaadinIcon.PLUS.create());
        addButton.getStyle().set("background-color", "#007bff")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "10px 20px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        addButton.addClickListener(event -> addAsset());

        HorizontalLayout formLayout = new HorizontalLayout(nameField, valueField, categoryField, interestRateField, addButton);
        formLayout.setWidthFull();
        formLayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
        formLayout.setAlignItems(Alignment.CENTER);

        // Initialize the total assets card
        totalAssetsValue = new H2("$ 0.00");
        totalAssetsCard = createDashboardCard("Total Assets", totalAssetsValue);

        // Initialize the total change in assets card
        totalChangeValue = new H2();
        totalChangeCard = createDashboardCard("Total Change in Assets (Past Annum)", totalChangeValue);

        HorizontalLayout cardsLayout = new HorizontalLayout(totalAssetsCard, totalChangeCard);
        cardsLayout.setWidthFull();

        VerticalLayout topLayout = new VerticalLayout(cardsLayout);
        topLayout.setWidthFull();
        topLayout.setPadding(false);
        topLayout.setSpacing(false);

        VerticalLayout mainLayout = new VerticalLayout(topLayout, title, formLayout, assetLayout);
        mainLayout.setSizeFull();
        mainLayout.getStyle().set("background-color", "#f7f7f7");

        add(mainLayout);
    }

    private void configureFormFields() {
        nameField.setPlaceholder("Enter asset name...");
        nameField.getStyle().set("width", "200px");
        
        valueField.setPlaceholder("Enter value...");
        valueField.getStyle().set("width", "150px");
        
        categoryField.setPlaceholder("Enter category...");
        categoryField.getStyle().set("width", "150px");

        interestRateField.setPlaceholder("Enter interest rate...");
        interestRateField.getStyle().set("width", "150px");
    }

    private void listAssets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Asset> assets = assetService.getAssetsByUserId(userId);

        assetLayout.removeAll();

        if (assets == null || assets.isEmpty()) {
            Notification.show("No assets found.", 3000, Notification.Position.TOP_CENTER);
        } else {
            for (Asset asset : assets) {
                assetLayout.add(createAssetCard(asset));
            }
        }
    }

    private VerticalLayout createAssetCard(Asset asset) {
        VerticalLayout cardLayout = new VerticalLayout();
        cardLayout.setWidth("300px");
        cardLayout.getStyle().set("border", "1px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("box-shadow", "0px 4px 8px rgba(0, 0, 0, 0.1)")
                .set("background-color", "#ffffff");

        Icon assetIcon = VaadinIcon.AIRPLANE.create(); // Replace with an appropriate icon
        assetIcon.setSize("24px");
        assetIcon.getStyle().set("color", "#007bff");

        Span name = new Span("Name: " + asset.getName());
        Span value = new Span("Value: $" + asset.getValue().toString());
        Span category = new Span("Category: " + asset.getCategory());
        Span interestRate = new Span("Interest Rate: " + asset.getInterestRate() + "%");

        Button deleteButton = new Button("Delete", VaadinIcon.TRASH.create());
        deleteButton.getStyle().set("background-color", "#dc3545")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        deleteButton.addClickListener(event -> {
            assetService.deleteAsset(asset.getId());
            Notification.show("Asset deleted successfully", 3000, Notification.Position.TOP_CENTER);
            listAssets();
            updateTotalAssets();
            updateTotalChangeInAssets();
        });

        Button editButton = new Button("Edit", VaadinIcon.EDIT.create());
        editButton.getStyle().set("background-color", "#28a745")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        editButton.addClickListener(event -> showEditDialog(asset));

        HorizontalLayout topLayout = new HorizontalLayout(assetIcon, name);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(FlexLayout.JustifyContentMode.START);
        topLayout.setWidth("100%");

        cardLayout.add(topLayout, value, category, interestRate, editButton, deleteButton);
        return cardLayout;
    }

    private void showEditDialog(Asset asset) {
        Dialog editDialog = new Dialog();
        editDialog.setWidth("400px");

        TextField nameField = new TextField("Asset Name");
        nameField.setValue(asset.getName());

        NumberField valueField = new NumberField("Value ($)");
        valueField.setValue(asset.getValue().doubleValue());

        TextField categoryField = new TextField("Category");
        categoryField.setValue(asset.getCategory());

        NumberField interestRateField = new NumberField("Interest Rate (%)");
        interestRateField.setValue(asset.getInterestRate() != null ? asset.getInterestRate().doubleValue() : 0);

        Button saveButton = new Button("Save", VaadinIcon.CHECK.create());
        saveButton.getStyle().set("background-color", "#007bff")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        saveButton.addClickListener(event -> {
            Asset updatedAsset = new Asset();
            updatedAsset.setId(asset.getId());
            updatedAsset.setName(nameField.getValue());
            updatedAsset.setValue(BigDecimal.valueOf(valueField.getValue()));
            updatedAsset.setCategory(categoryField.getValue());
            updatedAsset.setInterestRate(BigDecimal.valueOf(interestRateField.getValue()));

            assetService.updateAsset(updatedAsset);
            Notification.show("Asset updated successfully", 3000, Notification.Position.TOP_CENTER);
            editDialog.close();
            listAssets();
            updateTotalAssets();
            updateTotalChangeInAssets();
        });

        Button cancelButton = new Button("Cancel", VaadinIcon.CLOSE.create());
        cancelButton.getStyle().set("background-color", "#6c757d")
                .set("color", "#ffffff")
                .set("border-radius", "5px")
                .set("padding", "8px 16px")
                .set("font-weight", "500")
                .set("border", "none")
                .set("box-shadow", "0px 2px 4px rgba(0, 0, 0, 0.1)");

        cancelButton.addClickListener(event -> editDialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(nameField, valueField, categoryField, interestRateField, new HorizontalLayout(saveButton, cancelButton));
        dialogLayout.setSizeUndefined();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);

        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void addAsset() {
        try {
            String name = nameField.getValue();
            Double valueText = valueField.getValue();
            String category = categoryField.getValue();
            Double interestRateText = interestRateField.getValue();
    
            // Validate that fields are not empty
            if (name.isEmpty() || valueText == null || category.isEmpty()) {
                Notification.show("Please fill in all fields", 3000, Notification.Position.TOP_CENTER);
                return;
            }
    
            BigDecimal value = BigDecimal.valueOf(valueText);
            BigDecimal interestRate = (interestRateText != null) ? BigDecimal.valueOf(interestRateText) : null;
    
            Asset asset = new Asset();
            asset.setName(name);
            asset.setValue(value);
            asset.setCategory(category);
            asset.setInterestRate(interestRate);
            asset.setUser(userService.findUserById(sessionService.getLoggedInUserId()));
    
            assetService.addAsset(asset);
            Notification.show("Asset added successfully", 3000, Notification.Position.TOP_CENTER);
            
            clearForm();
            listAssets();
            updateTotalAssets();
            updateTotalChangeInAssets();
        } catch (NumberFormatException e) {
            Notification.show("Please enter a valid value", 3000, Notification.Position.TOP_CENTER);
        }
    }
    
    // Method to clear the form fields
    private void clearForm() {
        nameField.clear();
        valueField.clear();
        categoryField.clear();
        interestRateField.clear();
    }

    private void updateTotalAssets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Asset> assets = assetService.getAssetsByUserId(userId);
        BigDecimal totalAssets = assets.stream()
            .map(Asset::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalAssetsValue.setText("$ " + totalAssets.toString());
    }

    private void updateTotalChangeInAssets() {
        Long userId = sessionService.getLoggedInUserId();
        BigDecimal totalChange = BigDecimal.ZERO;
        String changeText = totalChange.compareTo(BigDecimal.ZERO) >= 0
                ? "+$ " + totalChange.toString()
                : "-$ " + totalChange.abs().toString();
        String color = totalChange.compareTo(BigDecimal.ZERO) >= 0 ? "green" : "red";
        totalChangeValue.setText(changeText);
        totalChangeValue.getStyle().set("color", color);
    }

    private Div createDashboardCard(String title, H2 value) {
        Div card = new Div();
        card.setWidthFull(); // Increase width here
        card.getStyle().set("border", "1px solid #e0e0e0")
                .set("border-radius", "8px")
                .set("box-shadow", "0px 4px 8px rgba(0, 0, 0, 0.1)")
                .set("background-color", "#ffffff")
                .set("padding", "16px")
                .set("text-align", "center");
    
        H2 cardTitle = new H2(title);
        cardTitle.getStyle().set("color", "#333").set("font-weight", "600").set("margin", "0");
    
        Div content = new Div();
        content.add(cardTitle, value);
        content.setSizeUndefined();
    
        card.add(content);
        return card;
    }
}

        