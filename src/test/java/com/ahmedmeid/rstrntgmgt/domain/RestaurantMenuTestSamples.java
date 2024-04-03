package com.ahmedmeid.rstrntgmgt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RestaurantMenuTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RestaurantMenu getRestaurantMenuSample1() {
        return new RestaurantMenu().id(1L).menuName("menuName1").menuDescription("menuDescription1");
    }

    public static RestaurantMenu getRestaurantMenuSample2() {
        return new RestaurantMenu().id(2L).menuName("menuName2").menuDescription("menuDescription2");
    }

    public static RestaurantMenu getRestaurantMenuRandomSampleGenerator() {
        return new RestaurantMenu()
            .id(longCount.incrementAndGet())
            .menuName(UUID.randomUUID().toString())
            .menuDescription(UUID.randomUUID().toString());
    }
}
