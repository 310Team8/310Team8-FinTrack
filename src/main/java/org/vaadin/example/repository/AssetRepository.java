package org.vaadin.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.model.Asset;

import java.util.List;

/**
 * Repository interface for managing {@link Asset} entities.
 * This interface extends {@link JpaRepository}, providing CRUD operations and custom queries.
 */
public interface AssetRepository extends JpaRepository<Asset, Long> {

    /**
     * Finds a list of Assets associated with a specific user ID.
     *
     * @param userId the ID of the user whose Assets are to be retrieved
     * @return a list of Assets associated with the specified user ID
     */
    List<Asset> findByUserId(Long userId);
}
