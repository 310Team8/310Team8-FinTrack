package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Asset;
import org.vaadin.example.service.AssetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@Route(value = "asset", layout = MainLayout.class)
public class AssetView extends VerticalLayout {

    private final Grid<Asset> grid = new Grid<>(Asset.class);
    private final TextField nameField = new TextField("Asset Name");
    private final TextField valueField = new TextField("Value");

    private final AssetService assetService;
    private final SessionService sessionService;
    private final UserService userService;

    // Constructor Injection
    public AssetView(AssetService assetService, SessionService sessionService, UserService userService) {
        this.assetService = assetService;
        this.sessionService = sessionService;
        this.userService = userService;

        configureGrid();

        Button addButton = new Button("Add Asset", event -> addAsset());
        Button deleteButton = new Button("Delete Asset", event -> deleteAsset());

        HorizontalLayout formLayout = new HorizontalLayout(nameField, valueField, addButton, deleteButton);
        add(formLayout, grid);

        listAssets();
    }

    private void configureGrid() {
        // Exclude id and user fields from being displayed
        grid.setColumns("name", "value");
        grid.getColumnByKey("value").setHeader("Value ($)"); // Customize header
    }

    private void listAssets() {
        Long userId = sessionService.getLoggedInUserId();
        List<Asset> assets = assetService.getAssetsByUserId(userId);
        grid.setItems(assets);
    }

    private void addAsset() {
        try {
            String name = nameField.getValue();
            String valueText = valueField.getValue();
            BigDecimal value = new BigDecimal(valueText);

            Asset asset = new Asset();
            asset.setName(name);
            asset.setValue(value);
            asset.setUser(userService.findUserById(sessionService.getLoggedInUserId()));

            assetService.addAsset(asset);
            Notification.show("Asset added successfully", 3000, Notification.Position.TOP_CENTER);
            
            clearForm();
            listAssets();
        } catch (NumberFormatException e) {
            Notification.show("Please enter a valid value", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void deleteAsset() {
        Asset selectedAsset = grid.asSingleSelect().getValue();
        if (selectedAsset != null) {
            assetService.deleteAsset(selectedAsset.getId());
            Notification.show("Asset deleted successfully", 3000, Notification.Position.TOP_CENTER);
            listAssets();
        } else {
            Notification.show("Please select an asset to delete", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        nameField.clear();
        valueField.clear();
    }
}
