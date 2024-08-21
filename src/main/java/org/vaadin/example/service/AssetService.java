package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.example.model.Asset;
import org.vaadin.example.repository.AssetRepository;

import java.util.List;

/**
 * Service class for managing asset-related operations.
 * This class interacts with the {@link AssetRepository} to perform CRUD operations on {@link Asset} entities.
 */
@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    /**
     * Retrieves a list of assets associated with a specific user ID.
     *
     * @param userId the ID of the user whose assets are to be retrieved
     * @return a list of assets associated with the specified user ID
     */
    public List<Asset> getAssetsByUserId(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    /**
     * Adds a new asset to the repository.
     *
     * @param asset the asset object to be added
     * @return the newly added asset object
     */
    public Asset addAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    /**
     * Finds an asset by its ID.
     *
     * @param id the ID of the asset to find
     * @return the asset object if found, or null if not found
     */
    public Asset findAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }

    /**
     * Deletes an asset by its ID.
     *
     * @param id the ID of the asset to be deleted
     */
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }
}
