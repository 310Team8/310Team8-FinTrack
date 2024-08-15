package org.vaadin.example.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.example.MainLayout;

@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        add("Welcome to the Dashboard!");
        // Additional dashboard components and features can be added here
    }
}
