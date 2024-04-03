package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.MenuCategoryTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.MenuItemTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.OrderItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = getMenuItemSample1();
        MenuItem menuItem2 = new MenuItem();
        assertThat(menuItem1).isNotEqualTo(menuItem2);

        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);

        menuItem2 = getMenuItemSample2();
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }

    @Test
    void menuCategoryTest() throws Exception {
        MenuItem menuItem = getMenuItemRandomSampleGenerator();
        MenuCategory menuCategoryBack = getMenuCategoryRandomSampleGenerator();

        menuItem.setMenuCategory(menuCategoryBack);
        assertThat(menuItem.getMenuCategory()).isEqualTo(menuCategoryBack);

        menuItem.menuCategory(null);
        assertThat(menuItem.getMenuCategory()).isNull();
    }

    @Test
    void orderItemTest() throws Exception {
        MenuItem menuItem = getMenuItemRandomSampleGenerator();
        OrderItem orderItemBack = getOrderItemRandomSampleGenerator();

        menuItem.setOrderItem(orderItemBack);
        assertThat(menuItem.getOrderItem()).isEqualTo(orderItemBack);
        assertThat(orderItemBack.getMenuItem()).isEqualTo(menuItem);

        menuItem.orderItem(null);
        assertThat(menuItem.getOrderItem()).isNull();
        assertThat(orderItemBack.getMenuItem()).isNull();
    }
}
