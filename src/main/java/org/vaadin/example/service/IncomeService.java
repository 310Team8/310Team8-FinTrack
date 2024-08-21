package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Income;
import org.vaadin.example.repository.IncomeRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing income-related operations.
 * This class interacts with the {@link IncomeRepository} to perform CRUD operations on {@link Income} entities.
 */
@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    /**
     * Retrieves a list of incomes associated with a specific user ID.
     *
     * @param userId the ID of the user whose incomes are to be retrieved
     * @return a list of incomes associated with the specified user ID
     */
    public List<Income> getIncomesByUserId(Long userId) {
        return incomeRepository.findByUserId(userId);
    }

    /**
     * Adds a new income to the repository.
     *
     * @param income the income object to be added
     * @return the newly added income object
     */
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    /**
     * Finds an income by its ID.
     *
     * @param id the ID of the income to find
     * @return the income object if found, or null if not found
     */
    public Income findIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an income by its ID.
     *
     * @param id the ID of the income to be deleted
     */
    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    /**
     * Calculates the total income for the current month for a specific user.
     *
     * @param userId the ID of the user whose total income is to be calculated
     * @return the total amount of income for the user in the current month
     */
    public BigDecimal getTotalIncomeForCurrentMonth(Long userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        return incomeRepository.findTotalIncomeForPeriod(userId, startOfMonth, endOfMonth);
    }

    /**
     * Updates an existing income in the repository.
     *
     * @param updatedIncome the income object with updated details
     * @return the updated income object
     */
    public Income updateIncome(Income updatedIncome) {
        Income existingIncome = incomeRepository.findById(updatedIncome.getId()).orElse(null);
        if (existingIncome != null) {
            existingIncome.setSource(updatedIncome.getSource());
            existingIncome.setAmount(updatedIncome.getAmount());
            existingIncome.setDate(updatedIncome.getDate());
            existingIncome.setPaymentFrequency(updatedIncome.getPaymentFrequency());
            return incomeRepository.save(existingIncome);
        }
        return null;
    }



}
