package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.model.Income;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for managing {@link Income} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface IncomeRepository extends JpaRepository<Income, Long> {

    /**
     * Finds a list of incomes associated with a specific user ID.
     *
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a list of incomes associated with the specified user ID
     */
    List<Income> findByUserId(Long userId);

    /**
     * Calculates the total income for a user within a specific period.
     *
     * @param userId the ID of the user whose total income is to be calculated
     * @param startDate the start date of the period
     * @param endDate the end date of the period
     * @return the total amount of income for the specified user within the period
     */
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId AND i.date BETWEEN :startDate AND :endDate")
    BigDecimal findTotalIncomeForPeriod(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
