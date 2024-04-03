package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.MenuCategoryTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantMenuTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RestaurantMenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestaurantMenu.class);
        RestaurantMenu restaurantMenu1 = getRestaurantMenuSample1();
        RestaurantMenu restaurantMenu2 = new RestaurantMenu();
        assertThat(restaurantMenu1).isNotEqualTo(restaurantMenu2);

        restaurantMenu2.setId(restaurantMenu1.getId());
        assertThat(restaurantMenu1).isEqualTo(restaurantMenu2);

        restaurantMenu2 = getRestaurantMenuSample2();
        assertThat(restaurantMenu1).isNotEqualTo(restaurantMenu2);
    }

    @Test
    void menuCategoryTest() throws Exception {
        RestaurantMenu restaurantMenu = getRestaurantMenuRandomSampleGenerator();
        MenuCategory menuCategoryBack = getMenuCategoryRandomSampleGenerator();

        restaurantMenu.addMenuCategory(menuCategoryBack);
        assertThat(restaurantMenu.getMenuCategories()).containsOnly(menuCategoryBack);
        assertThat(menuCategoryBack.getMenu()).isEqualTo(restaurantMenu);

        restaurantMenu.removeMenuCategory(menuCategoryBack);
        assertThat(restaurantMenu.getMenuCategories()).doesNotContain(menuCategoryBack);
        assertThat(menuCategoryBack.getMenu()).isNull();

        restaurantMenu.menuCategories(new HashSet<>(Set.of(menuCategoryBack)));
        assertThat(restaurantMenu.getMenuCategories()).containsOnly(menuCategoryBack);
        assertThat(menuCategoryBack.getMenu()).isEqualTo(restaurantMenu);

        restaurantMenu.setMenuCategories(new HashSet<>());
        assertThat(restaurantMenu.getMenuCategories()).doesNotContain(menuCategoryBack);
        assertThat(menuCategoryBack.getMenu()).isNull();
    }

    @Test
    void restaurantTest() throws Exception {
        RestaurantMenu restaurantMenu = getRestaurantMenuRandomSampleGenerator();
        Restaurant restaurantBack = getRestaurantRandomSampleGenerator();

        restaurantMenu.setRestaurant(restaurantBack);
        assertThat(restaurantMenu.getRestaurant()).isEqualTo(restaurantBack);

        restaurantMenu.restaurant(null);
        assertThat(restaurantMenu.getRestaurant()).isNull();
    }
}
