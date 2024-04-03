package com.ahmedmeid.rstrntgmgt.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DineInOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DineInOrder getDineInOrderSample1() {
        return new DineInOrder().id(1L).tableNumber(1);
    }

    public static DineInOrder getDineInOrderSample2() {
        return new DineInOrder().id(2L).tableNumber(2);
    }

    public static DineInOrder getDineInOrderRandomSampleGenerator() {
        return new DineInOrder().id(longCount.incrementAndGet()).tableNumber(intCount.incrementAndGet());
    }
}
