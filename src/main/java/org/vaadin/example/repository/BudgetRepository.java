package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
