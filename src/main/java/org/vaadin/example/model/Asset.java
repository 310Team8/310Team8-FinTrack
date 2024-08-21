package org.vaadin.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * Entity representing an asset.
 * An asset is associated with a user and has a name and value.
 */
@Entity
@Table(name = "asset")
public class Asset {
    /**
     * The unique identifier for the asset.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the asset.
     * Cannot be null.
     */
    @NotNull
    private String name;

    /**
     * The value of the asset.
     * Must be a positive value greater than 0.0.
     */
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal value;

    /**
     * The user associated with the asset.
     * Cannot be null.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and Setters

    /**
     * Gets the unique identifier for the asset.
     *
     * @return the ID of the asset
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the asset.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the asset.
     *
     * @return the name of the asset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the asset.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the asset.
     *
     * @return the value of the asset
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * Sets the value of the asset.
     *
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Gets the user associated with the asset.
     *
     * @return the user associated with the asset
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the asset.
     *
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
