package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.DineInOrderTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.MenuItemTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.OrderItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void menuItemTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        orderItem.setMenuItem(menuItemBack);
        assertThat(orderItem.getMenuItem()).isEqualTo(menuItemBack);

        orderItem.menuItem(null);
        assertThat(orderItem.getMenuItem()).isNull();
    }

    @Test
    void dineInOrderTest() throws Exception {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        DineInOrder dineInOrderBack = getDineInOrderRandomSampleGenerator();

        orderItem.setDineInOrder(dineInOrderBack);
        assertThat(orderItem.getDineInOrder()).isEqualTo(dineInOrderBack);

        orderItem.dineInOrder(null);
        assertThat(orderItem.getDineInOrder()).isNull();
    }
}
