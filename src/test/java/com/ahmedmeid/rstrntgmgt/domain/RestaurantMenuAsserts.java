package com.ahmedmeid.rstrntgmgt.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantMenuAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRestaurantMenuAllPropertiesEquals(RestaurantMenu expected, RestaurantMenu actual) {
        assertRestaurantMenuAutoGeneratedPropertiesEquals(expected, actual);
        assertRestaurantMenuAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRestaurantMenuAllUpdatablePropertiesEquals(RestaurantMenu expected, RestaurantMenu actual) {
        assertRestaurantMenuUpdatableFieldsEquals(expected, actual);
        assertRestaurantMenuUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRestaurantMenuAutoGeneratedPropertiesEquals(RestaurantMenu expected, RestaurantMenu actual) {
        assertThat(expected)
            .as("Verify RestaurantMenu auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRestaurantMenuUpdatableFieldsEquals(RestaurantMenu expected, RestaurantMenu actual) {
        assertThat(expected)
            .as("Verify RestaurantMenu relevant properties")
            .satisfies(e -> assertThat(e.getMenuName()).as("check menuName").isEqualTo(actual.getMenuName()))
            .satisfies(e -> assertThat(e.getMenuDescription()).as("check menuDescription").isEqualTo(actual.getMenuDescription()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRestaurantMenuUpdatableRelationshipsEquals(RestaurantMenu expected, RestaurantMenu actual) {
        assertThat(expected)
            .as("Verify RestaurantMenu relationships")
            .satisfies(e -> assertThat(e.getRestaurant()).as("check restaurant").isEqualTo(actual.getRestaurant()));
    }
}
