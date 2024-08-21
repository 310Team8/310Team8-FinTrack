package org.vaadin.example.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;
import org.vaadin.example.model.Budget;
import org.vaadin.example.service.AssetService;
import org.vaadin.example.service.BudgetService;
import org.vaadin.example.service.SessionService;
import org.vaadin.example.service.UserService;

@Route(value = "asset", layout = MainLayout.class)
public class AssetView extends VerticalLayout {

    private final AssetService assetService;
    private final SessionService sessionService;
    private final UserService userService;
    

    public AssetView(AssetService assetService, SessionService sessionService, UserService userService){
        this.assetService = assetService;
        this.sessionService = sessionService;
        this.userService = userService;

    
    }
}
