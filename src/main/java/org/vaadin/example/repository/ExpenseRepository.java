package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
