package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing {@link Expense} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    /**
     * Finds a list of expenses associated with a specific user ID.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @return a list of expenses associated with the specified user ID
     */
    List<Expense> findByUserId(Long userId);

    /**
     * Calculates the total expenses for a user within a specific period.
     *
     * @param userId the ID of the user whose total expenses are to be calculated
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return the total amount of expenses for the specified user within the period
     */
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalExpensesForPeriod(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Finds a list of expenses for a user within a specific period.
     *
     * @param userId the ID of the user whose expenses are to be retrieved
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return a list of expenses for the specified user within the period
     */
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
