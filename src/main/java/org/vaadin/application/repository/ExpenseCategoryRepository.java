package org.vaadin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.ExpenseCategory;

import java.util.List;

/**
 * Repository interface for managing {@link ExpenseCategory} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    /**
     * Finds a list of expense categories associated with a specific user ID.
     *
     * @param userId the ID of the user whose expense categories are to be retrieved
     * @return a list of expense categories associated with the specified user ID
     */
    List<ExpenseCategory> findByUserId(Long userId);
}
