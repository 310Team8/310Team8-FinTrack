package org.vaadin.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.application.model.BusinessProfile;
import org.vaadin.application.repository.BusinessProfileRepository;

import java.util.List;

/**
 * Service class for managing business profile-related operations. This class interacts with the {@link BusinessProfileRepository}
 * to perform CRUD operations on {@link BusinessProfile} entities.
 */
@Service
public class BusinessProfileService {
    
    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    /**
     * Retrieves a list of business profiles associated with a specific user ID.
     *
     * @param userId the ID of the user whose business profiles are to be retrieved
     * @return a list of business profiles associated with the specified user ID
     */
    public List<BusinessProfile> getBusinessProfilesByUserId(Long userId) {
        return businessProfileRepository.findByUserId(userId);
    }

    /**
     * Adds a new business profile to the repository.
     *
     * @param businessProfile the business profile object to be added
     * @return the newly added business profile object
     */
    public BusinessProfile addBusinessProfile(BusinessProfile businessProfile) {
        return businessProfileRepository.save(businessProfile);
    }

    /**
     * Updates an existing business profile.
     *
     * @param businessProfileId the ID of the business profile to update
     * @param updatedBusinessProfile the updated business profile object with new details
     * @return the updated business profile object, or throws an exception if not found
     */
    public BusinessProfile getBusinessProfileById(Long businessProfileId) {
        return businessProfileRepository.findById(businessProfileId).orElse(null);
    }

    /**
     * Updates an existing business profile.
     *
     * @param businessProfileId the ID of the business profile to update
     * @param updatedBusinessProfile the updated business profile object with new details
     * @return the updated business profile object, or throws an exception if not found
     */
    public BusinessProfile getLastBusinessProfileByUserId(Long userId) {
        List<BusinessProfile> businessProfiles = businessProfileRepository.findByUserId(userId);
        return businessProfiles.isEmpty() ? null : businessProfiles.get(businessProfiles.size() - 1);
    }

    /**
     * Updates an existing business profile.
     *
     * @param businessProfileId the ID of the business profile to update
     * @param updatedBusinessProfile the updated business profile object with new details
     * @return the updated business profile object, or throws an exception if not found
     */
    public BusinessProfile findBusinessProfileById(Long id) {
        return businessProfileRepository.findById(id).orElse(null);
    }

    /**
     * Updates an existing business profile.
     *
     * @param businessProfileId the ID of the business profile to update
     * @param updatedBusinessProfile the updated business profile object with new details
     * @return the updated business profile object, or throws an exception if not found
     */
    public void deleteBusinessProfile(Long id) {
        businessProfileRepository.deleteById(id);
    }
}