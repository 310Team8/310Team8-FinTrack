package org.vaadin.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.Invoice;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link Invoice} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    /**
     * Finds a list of Assets associated with a specific user ID.
     *
     * @param userId the ID of the user whose Assets are to be retrieved
     * @return a list of Assets associated with the specified user ID
     */
    List<Invoice> findByUserId(Long userId);
}
