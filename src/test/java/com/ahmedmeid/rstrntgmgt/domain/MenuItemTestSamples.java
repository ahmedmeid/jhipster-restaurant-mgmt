package com.ahmedmeid.rstrntgmgt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MenuItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static MenuItem getMenuItemSample1() {
        return new MenuItem().id(1L).itemName("itemName1").itemDescription("itemDescription1").ingredients("ingredients1");
    }

    public static MenuItem getMenuItemSample2() {
        return new MenuItem().id(2L).itemName("itemName2").itemDescription("itemDescription2").ingredients("ingredients2");
    }

    public static MenuItem getMenuItemRandomSampleGenerator() {
        return new MenuItem()
            .id(longCount.incrementAndGet())
            .itemName(UUID.randomUUID().toString())
            .itemDescription(UUID.randomUUID().toString())
            .ingredients(UUID.randomUUID().toString());
    }
}
