package org.vaadin.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/** 
 * Entity representing a business profile.
 * A business profile is associated with a user and contains information about a business,
 * such as its name, type, description, address, city, phone number, and person in charge.
 */
@Entity
@Table(name = "business_profiles")
public class BusinessProfile{
    
    /**
     * The unique identifier for the business profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the business.
     * Cannot be null.
     */
    @NotNull
    private String businessName;

    /**
     * The type of business.
     * Cannot be null.
     */
    @NotNull
    private String businessType;

    /**
     * A description of the business.
     * Cannot be null.
     */
    @NotNull
    private String businessDescription;

    /**
     * The address of the business.
     * Cannot be null.
     */
    @NotNull
    private String businessAddress;

    /**
     * The city where the business is located.
     * Cannot be null.
     */
    @NotNull
    private String businessCity;

    /**
     * The phone number of the business.
     * Cannot be null.
     */
    @NotNull
    private String phoneNumber;

    /**
     * The person in charge of the business.
     * Cannot be null.
     */
    @NotNull
    private String personInCharge;

    /**
     * The user associated with the business profile.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessCity() {
        return businessCity;
    }

    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
