package com.ahmedmeid.rstrntgmgt.domain;

import static com.ahmedmeid.rstrntgmgt.domain.MenuCategoryTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.MenuItemTestSamples.*;
import static com.ahmedmeid.rstrntgmgt.domain.RestaurantMenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.ahmedmeid.rstrntgmgt.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MenuCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuCategory.class);
        MenuCategory menuCategory1 = getMenuCategorySample1();
        MenuCategory menuCategory2 = new MenuCategory();
        assertThat(menuCategory1).isNotEqualTo(menuCategory2);

        menuCategory2.setId(menuCategory1.getId());
        assertThat(menuCategory1).isEqualTo(menuCategory2);

        menuCategory2 = getMenuCategorySample2();
        assertThat(menuCategory1).isNotEqualTo(menuCategory2);
    }

    @Test
    void menuItemTest() throws Exception {
        MenuCategory menuCategory = getMenuCategoryRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        menuCategory.addMenuItem(menuItemBack);
        assertThat(menuCategory.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getMenuCategory()).isEqualTo(menuCategory);

        menuCategory.removeMenuItem(menuItemBack);
        assertThat(menuCategory.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getMenuCategory()).isNull();

        menuCategory.menuItems(new HashSet<>(Set.of(menuItemBack)));
        assertThat(menuCategory.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getMenuCategory()).isEqualTo(menuCategory);

        menuCategory.setMenuItems(new HashSet<>());
        assertThat(menuCategory.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getMenuCategory()).isNull();
    }

    @Test
    void menuTest() throws Exception {
        MenuCategory menuCategory = getMenuCategoryRandomSampleGenerator();
        RestaurantMenu restaurantMenuBack = getRestaurantMenuRandomSampleGenerator();

        menuCategory.setMenu(restaurantMenuBack);
        assertThat(menuCategory.getMenu()).isEqualTo(restaurantMenuBack);

        menuCategory.menu(null);
        assertThat(menuCategory.getMenu()).isNull();
    }
}
