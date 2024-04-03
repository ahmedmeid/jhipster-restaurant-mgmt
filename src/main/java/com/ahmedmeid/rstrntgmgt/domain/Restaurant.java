package com.ahmedmeid.rstrntgmgt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "restaurant_description")
    private String restaurantDescription;

    @Column(name = "no_of_tables")
    private Integer noOfTables;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuCategories", "restaurant" }, allowSetters = true)
    private Set<RestaurantMenu> restaurantMenus = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orderItems", "restaurant" }, allowSetters = true)
    private Set<DineInOrder> dineInOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public Restaurant restaurantName(String restaurantName) {
        this.setRestaurantName(restaurantName);
        return this;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantDescription() {
        return this.restaurantDescription;
    }

    public Restaurant restaurantDescription(String restaurantDescription) {
        this.setRestaurantDescription(restaurantDescription);
        return this;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }

    public Integer getNoOfTables() {
        return this.noOfTables;
    }

    public Restaurant noOfTables(Integer noOfTables) {
        this.setNoOfTables(noOfTables);
        return this;
    }

    public void setNoOfTables(Integer noOfTables) {
        this.noOfTables = noOfTables;
    }

    public Set<RestaurantMenu> getRestaurantMenus() {
        return this.restaurantMenus;
    }

    public void setRestaurantMenus(Set<RestaurantMenu> restaurantMenus) {
        if (this.restaurantMenus != null) {
            this.restaurantMenus.forEach(i -> i.setRestaurant(null));
        }
        if (restaurantMenus != null) {
            restaurantMenus.forEach(i -> i.setRestaurant(this));
        }
        this.restaurantMenus = restaurantMenus;
    }

    public Restaurant restaurantMenus(Set<RestaurantMenu> restaurantMenus) {
        this.setRestaurantMenus(restaurantMenus);
        return this;
    }

    public Restaurant addRestaurantMenu(RestaurantMenu restaurantMenu) {
        this.restaurantMenus.add(restaurantMenu);
        restaurantMenu.setRestaurant(this);
        return this;
    }

    public Restaurant removeRestaurantMenu(RestaurantMenu restaurantMenu) {
        this.restaurantMenus.remove(restaurantMenu);
        restaurantMenu.setRestaurant(null);
        return this;
    }

    public Set<DineInOrder> getDineInOrders() {
        return this.dineInOrders;
    }

    public void setDineInOrders(Set<DineInOrder> dineInOrders) {
        if (this.dineInOrders != null) {
            this.dineInOrders.forEach(i -> i.setRestaurant(null));
        }
        if (dineInOrders != null) {
            dineInOrders.forEach(i -> i.setRestaurant(this));
        }
        this.dineInOrders = dineInOrders;
    }

    public Restaurant dineInOrders(Set<DineInOrder> dineInOrders) {
        this.setDineInOrders(dineInOrders);
        return this;
    }

    public Restaurant addDineInOrder(DineInOrder dineInOrder) {
        this.dineInOrders.add(dineInOrder);
        dineInOrder.setRestaurant(this);
        return this;
    }

    public Restaurant removeDineInOrder(DineInOrder dineInOrder) {
        this.dineInOrders.remove(dineInOrder);
        dineInOrder.setRestaurant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return getId() != null && getId().equals(((Restaurant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", restaurantName='" + getRestaurantName() + "'" +
            ", restaurantDescription='" + getRestaurantDescription() + "'" +
            ", noOfTables=" + getNoOfTables() +
            "}";
    }
}
