package com.ahmedmeid.rstrntgmgt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MenuCategory getMenuCategorySample1() {
        return new MenuCategory().id(1L).categoryName("categoryName1").categoryDescription("categoryDescription1");
    }

    public static MenuCategory getMenuCategorySample2() {
        return new MenuCategory().id(2L).categoryName("categoryName2").categoryDescription("categoryDescription2");
    }

    public static MenuCategory getMenuCategoryRandomSampleGenerator() {
        return new MenuCategory()
            .id(longCount.incrementAndGet())
            .categoryName(UUID.randomUUID().toString())
            .categoryDescription(UUID.randomUUID().toString());
    }
}
