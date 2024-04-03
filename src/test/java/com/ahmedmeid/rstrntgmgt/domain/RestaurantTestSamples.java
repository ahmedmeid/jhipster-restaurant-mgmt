package com.ahmedmeid.rstrntgmgt.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RestaurantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Restaurant getRestaurantSample1() {
        return new Restaurant().id(1L).restaurantName("restaurantName1").restaurantDescription("restaurantDescription1").noOfTables(1);
    }

    public static Restaurant getRestaurantSample2() {
        return new Restaurant().id(2L).restaurantName("restaurantName2").restaurantDescription("restaurantDescription2").noOfTables(2);
    }

    public static Restaurant getRestaurantRandomSampleGenerator() {
        return new Restaurant()
            .id(longCount.incrementAndGet())
            .restaurantName(UUID.randomUUID().toString())
            .restaurantDescription(UUID.randomUUID().toString())
            .noOfTables(intCount.incrementAndGet());
    }
}
