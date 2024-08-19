package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.FinancialGoal;

import java.util.List;

/**
 * Repository interface for managing {@link FinancialGoal} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {

    /**
     * Finds a list of financial goals associated with a specific user ID.
     *
     * @param userId the ID of the user whose financial goals are to be retrieved
     * @return a list of financial goals associated with the specified user ID
     */
    List<FinancialGoal> findByUserId(Long userId);
}
