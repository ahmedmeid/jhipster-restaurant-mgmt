package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.DineInOrderTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantMenuTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RestaurantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurant.class);
        Restaurant restaurant1 = getRestaurantSample1();
        Restaurant restaurant2 = new Restaurant();
        assertThat(restaurant1).isNotEqualTo(restaurant2);

        restaurant2.setId(restaurant1.getId());
        assertThat(restaurant1).isEqualTo(restaurant2);

        restaurant2 = getRestaurantSample2();
        assertThat(restaurant1).isNotEqualTo(restaurant2);
    }

    @Test
    void restaurantMenuTest() throws Exception {
        Restaurant restaurant = getRestaurantRandomSampleGenerator();
        RestaurantMenu restaurantMenuBack = getRestaurantMenuRandomSampleGenerator();

        restaurant.addRestaurantMenu(restaurantMenuBack);
        assertThat(restaurant.getRestaurantMenus()).containsOnly(restaurantMenuBack);
        assertThat(restaurantMenuBack.getRestaurant()).isEqualTo(restaurant);

        restaurant.removeRestaurantMenu(restaurantMenuBack);
        assertThat(restaurant.getRestaurantMenus()).doesNotContain(restaurantMenuBack);
        assertThat(restaurantMenuBack.getRestaurant()).isNull();

        restaurant.restaurantMenus(new HashSet<>(Set.of(restaurantMenuBack)));
        assertThat(restaurant.getRestaurantMenus()).containsOnly(restaurantMenuBack);
        assertThat(restaurantMenuBack.getRestaurant()).isEqualTo(restaurant);

        restaurant.setRestaurantMenus(new HashSet<>());
        assertThat(restaurant.getRestaurantMenus()).doesNotContain(restaurantMenuBack);
        assertThat(restaurantMenuBack.getRestaurant()).isNull();
    }

    @Test
    void dineInOrderTest() throws Exception {
        Restaurant restaurant = getRestaurantRandomSampleGenerator();
        DineInOrder dineInOrderBack = getDineInOrderRandomSampleGenerator();

        restaurant.addDineInOrder(dineInOrderBack);
        assertThat(restaurant.getDineInOrders()).containsOnly(dineInOrderBack);
        assertThat(dineInOrderBack.getRestaurant()).isEqualTo(restaurant);

        restaurant.removeDineInOrder(dineInOrderBack);
        assertThat(restaurant.getDineInOrders()).doesNotContain(dineInOrderBack);
        assertThat(dineInOrderBack.getRestaurant()).isNull();

        restaurant.dineInOrders(new HashSet<>(Set.of(dineInOrderBack)));
        assertThat(restaurant.getDineInOrders()).containsOnly(dineInOrderBack);
        assertThat(dineInOrderBack.getRestaurant()).isEqualTo(restaurant);

        restaurant.setDineInOrders(new HashSet<>());
        assertThat(restaurant.getDineInOrders()).doesNotContain(dineInOrderBack);
        assertThat(dineInOrderBack.getRestaurant()).isNull();
    }
}
