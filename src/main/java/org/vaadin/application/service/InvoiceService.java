package org.vaadin.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.Invoice;
import org.vaadin.application.repository.InvoiceRepository;

/**
 * Service class for managing invoice-related operations. This class interacts with the {@link InvoiceRepository}
 * to perform CRUD operations on {@link Invoice} entities.
 */
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * Retrieves a list of invoices associated with a specific user ID.
     *
     * @param userId the ID of the user whose invoices are to be retrieved
     * @return a list of invoices associated with the specified user ID
     */
    public List<Invoice> getInvoicesByUserId(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    /**
     * Retrieves all invoices.
     *
     * @return a list of all invoices
     */
    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the ID of the invoice to be retrieved
     * @return an Optional containing the found invoice, or empty if not found
     */
    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    /**
     * Adds a new invoice to the repository.
     *
     * @param invoice the invoice object to be added
     * @return the newly added invoice object
     */
    public Invoice addInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    /**
     * Updates an existing invoice.
     *
     * @param invoiceId the ID of the invoice to update
     * @param updatedInvoice the updated invoice object with new details
     * @return the updated invoice object, or throws an exception if not found
     */
    public Invoice updateInvoice(Long invoiceId, Invoice updatedInvoice) {
        Optional<Invoice> existingInvoiceOpt = invoiceRepository.findById(invoiceId);

        if (existingInvoiceOpt.isPresent()) {
            Invoice existingInvoice = existingInvoiceOpt.get();

            // Update the existing invoice with the new values
            existingInvoice.setInvoiceNumber(updatedInvoice.getInvoiceNumber());
            existingInvoice.setRecipientName(updatedInvoice.getRecipientName());
            existingInvoice.setAmount(updatedInvoice.getAmount());
            existingInvoice.setIssueDate(updatedInvoice.getIssueDate());
            existingInvoice.setDueDate(updatedInvoice.getDueDate());
            existingInvoice.setDescription(updatedInvoice.getDescription());
            existingInvoice.setStatus(updatedInvoice.getStatus());

            // Save the updated invoice
            return invoiceRepository.save(existingInvoice);
        } else {
            throw new RuntimeException("Invoice not found with ID: " + invoiceId);
        }
    }

    /**
     * Deletes an invoice by its ID.
     *
     * @param id the ID of the invoice to be deleted
     */
    public void deleteInvoice(Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Invoice not found with ID: " + id);
        }
    }

    /**
     * Retrieves all invoices that are marked as overdue (i.e., past their due date).
     *
     * @return a list of overdue invoices
     */
    public List<Invoice> getOverdueInvoices() {
        return invoiceRepository.findAll().stream()
                .filter(invoice -> "Overdue".equalsIgnoreCase(invoice.getStatus()))
                .toList();
    }

    /**
     * Marks an invoice as paid by updating its status.
     *
     * @param id the ID of the invoice to be marked as paid
     * @return the updated invoice object with status set to "Paid"
     */
    public Invoice markAsPaid(Long id) {
        Optional<Invoice> existingInvoiceOpt = invoiceRepository.findById(id);

        if (existingInvoiceOpt.isPresent()) {
            Invoice existingInvoice = existingInvoiceOpt.get();
            existingInvoice.setStatus("Paid");
            return invoiceRepository.save(existingInvoice);
        } else {
            throw new RuntimeException("Invoice not found with ID: " + id);
        }
    }
}
