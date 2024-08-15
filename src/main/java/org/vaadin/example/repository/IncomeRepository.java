package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.Income;

public interface IncomeRepository extends JpaRepository<Income, Long> {
}
