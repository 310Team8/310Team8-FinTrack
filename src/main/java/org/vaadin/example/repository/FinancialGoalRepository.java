package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.FinancialGoal;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
}
