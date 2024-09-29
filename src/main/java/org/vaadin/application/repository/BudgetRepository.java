package org.vaadin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.Budget;

import java.util.List;

/**
 * Repository interface for managing {@link Budget} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    /**
     * Finds a list of budgets associated with a specific user ID.
     *
     * @param userId the ID of the user whose budgets are to be retrieved
     * @return a list of budgets associated with the specified user ID
     */
    List<Budget> findByUserId(Long userId);
}
