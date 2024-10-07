package org.vaadin.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.application.model.BusinessProfile;

/**
 * repository interface for managing {@link BusinessProfile} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {

    /**
     * Finds a list of BusinessProfiles associated with a specific user ID.
     *
     * @param userId the ID of the user whose BusinessProfiles are to be retrieved
     * @return a list of BusinessProfiles associated with the specified user ID
     */
    List<BusinessProfile> findByUserId(Long userId);
}
