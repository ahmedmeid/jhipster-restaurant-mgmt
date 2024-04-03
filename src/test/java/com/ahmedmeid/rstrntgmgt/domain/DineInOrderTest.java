package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.DineInOrderTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.OrderItemTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DineInOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DineInOrder.class);
        DineInOrder dineInOrder1 = getDineInOrderSample1();
        DineInOrder dineInOrder2 = new DineInOrder();
        assertThat(dineInOrder1).isNotEqualTo(dineInOrder2);

        dineInOrder2.setId(dineInOrder1.getId());
        assertThat(dineInOrder1).isEqualTo(dineInOrder2);

        dineInOrder2 = getDineInOrderSample2();
        assertThat(dineInOrder1).isNotEqualTo(dineInOrder2);
    }

    @Test
    void orderItemTest() throws Exception {
        DineInOrder dineInOrder = getDineInOrderRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        dineInOrder.addOrderItem(orderItemBack);
        assertThat(dineInOrder.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getDineInOrder()).isEqualTo(dineInOrder);

        dineInOrder.removeOrderItem(orderItemBack);
        assertThat(dineInOrder.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getDineInOrder()).isNull();

        dineInOrder.orderItems(new HashSet<>(Set.of(orderItemBack)));
        assertThat(dineInOrder.getOrderItems()).containsOnly(orderItemBack);
        assertThat(orderItemBack.getDineInOrder()).isEqualTo(dineInOrder);

        dineInOrder.setOrderItems(new HashSet<>());
        assertThat(dineInOrder.getOrderItems()).doesNotContain(orderItemBack);
        assertThat(orderItemBack.getDineInOrder()).isNull();
    }

    @Test
    void restaurantTest() throws Exception {
        DineInOrder dineInOrder = getDineInOrderRandomSampleGenerator();
        Restaurant restaurantBack = getRestaurantRandomSampleGenerator();

        dineInOrder.setRestaurant(restaurantBack);
        assertThat(dineInOrder.getRestaurant()).isEqualTo(restaurantBack);

        dineInOrder.restaurant(null);
        assertThat(dineInOrder.getRestaurant()).isNull();
    }
}
