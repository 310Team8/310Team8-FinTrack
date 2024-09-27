package org.vaadin.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vaadin.application.model.Asset;
import org.vaadin.application.service.AssetService;

import java.util.List;

/**
 * Rest controller for managing asset-related operations.
 * This controller provides endpoints to get assets by user ID, add a new asset, 
 * and delete an existing asset.
 */
@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    /**
     * Retrieves a list of assets associated with a specific user ID.
     *
     * @param userId the ID of the user whose assets are to be retrieved
     * @return a list of assets associated with the specified user ID
     */
    @GetMapping("/user/{userId}")
    public List<Asset> getAssetsByUserId(@PathVariable Long userId) {
        return assetService.getAssetsByUserId(userId);
    }

    /**
     * Adds a new asset.
     *
     * @param asset the asset object to be added
     * @return the newly added asset object
     */
    @PostMapping("/add")
    public Asset addAsset(@RequestBody Asset asset) {
        return assetService.addAsset(asset);
    }

    /**
     * Deletes an asset by its ID.
     *
     * @param id the ID of the asset to be deleted
     */
    @DeleteMapping("/delete/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}
