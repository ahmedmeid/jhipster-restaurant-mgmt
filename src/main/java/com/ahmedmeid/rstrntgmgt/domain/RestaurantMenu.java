package com.ahmedmeid.rstrntgmgt.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RestaurantMenu.
 */
@Entity
@Table(name = "restaurant_menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RestaurantMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_description")
    private String menuDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "menu" }, allowSetters = true)
    private Set<MenuCategory> menuCategories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "restaurantMenus", "dineInOrders" }, allowSetters = true)
    private Restaurant restaurant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RestaurantMenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return this.menuName;
    }

    public RestaurantMenu menuName(String menuName) {
        this.setMenuName(menuName);
        return this;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return this.menuDescription;
    }

    public RestaurantMenu menuDescription(String menuDescription) {
        this.setMenuDescription(menuDescription);
        return this;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public Set<MenuCategory> getMenuCategories() {
        return this.menuCategories;
    }

    public void setMenuCategories(Set<MenuCategory> menuCategories) {
        if (this.menuCategories != null) {
            this.menuCategories.forEach(i -> i.setMenu(null));
        }
        if (menuCategories != null) {
            menuCategories.forEach(i -> i.setMenu(this));
        }
        this.menuCategories = menuCategories;
    }

    public RestaurantMenu menuCategories(Set<MenuCategory> menuCategories) {
        this.setMenuCategories(menuCategories);
        return this;
    }

    public RestaurantMenu addMenuCategory(MenuCategory menuCategory) {
        this.menuCategories.add(menuCategory);
        menuCategory.setMenu(this);
        return this;
    }

    public RestaurantMenu removeMenuCategory(MenuCategory menuCategory) {
        this.menuCategories.remove(menuCategory);
        menuCategory.setMenu(null);
        return this;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public RestaurantMenu restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantMenu)) {
            return false;
        }
        return getId() != null && getId().equals(((RestaurantMenu) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurantMenu{" +
            "id=" + getId() +
            ", menuName='" + getMenuName() + "'" +
            ", menuDescription='" + getMenuDescription() + "'" +
            "}";
    }
}
