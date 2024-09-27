package org.vaadin.application.service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.Asset;
import org.vaadin.application.repository.AssetRepository;

/**
 * Service class for managing asset-related operations. This class interacts with the {@link
 * AssetRepository} to perform CRUD operations on {@link Asset} entities.
 */
@Service
public class AssetService {

  @Autowired private AssetRepository assetRepository;

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

  /**
   * Calculates the total value of the user's assets from the previous year. This method is not yet
   * implemented.
   *
   * @param userId the ID of the user whose total assets are to be calculated
   * @return the total value of the user's assets from the previous year
   * @throws UnsupportedOperationException if the method is not yet implemented
   */
  public BigDecimal getTotalAssetsLastYear(Long userId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getTotalAssetsLastYear'");
  }

  /**
   * Calculates the current total value of the user's assets. This method is not yet implemented.
   *
   * @param userId the ID of the user whose current total assets are to be calculated
   * @return the current total value of the user's assets
   * @throws UnsupportedOperationException if the method is not yet implemented
   */
  public BigDecimal getTotalAssetsCurrent(Long userId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getTotalAssetsCurrent'");
  }

  /**
   * Updates an existing asset with new information. If the asset exists, its details will be
   * updated and saved.
   *
   * @param updatedAsset the asset object containing the updated information
   * @return the updated asset if the asset was found and updated, or {@code null} if the asset does
   *     not exist
   */
  public Asset updateAsset(Asset updatedAsset) {
    // Find the existing asset by ID
    Asset existingAsset = findAssetById(updatedAsset.getId());
    if (existingAsset != null) {
      // Update the details of the existing asset
      existingAsset.setName(updatedAsset.getName());
      existingAsset.setValue(updatedAsset.getValue());
      existingAsset.setCategory(updatedAsset.getCategory());
      existingAsset.setInterestRate(updatedAsset.getInterestRate());

      // Save the updated asset to the repository
      return assetRepository.save(existingAsset);
    }
    return null; // Return null if the asset does not exist
  }
}
