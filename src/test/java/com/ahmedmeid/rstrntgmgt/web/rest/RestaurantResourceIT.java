package com.ahmedmeid.rstrntgmgt.web.rest;

import static com.ahmedmeid.rstrntgmgt.domain.RestaurantAsserts.*;
import static com.ahmedmeid.rstrntgmgt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ahmedmeid.rstrntgmgt.IntegrationTest;
import com.ahmedmeid.rstrntgmgt.domain.Restaurant;
import com.ahmedmeid.rstrntgmgt.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestaurantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RestaurantResourceIT {

    private static final String DEFAULT_RESTAURANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURANT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURANT_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NO_OF_TABLES = 1;
    private static final Integer UPDATED_NO_OF_TABLES = 2;

    private static final String ENTITY_API_URL = "/api/restaurants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .restaurantName(DEFAULT_RESTAURANT_NAME)
            .restaurantDescription(DEFAULT_RESTAURANT_DESCRIPTION)
            .noOfTables(DEFAULT_NO_OF_TABLES);
        return restaurant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurant createUpdatedEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .noOfTables(UPDATED_NO_OF_TABLES);
        return restaurant;
    }

    @BeforeEach
    public void initTest() {
        restaurant = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurant() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Restaurant
        var returnedRestaurant = om.readValue(
            restRestaurantMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurant)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Restaurant.class
        );

        // Validate the Restaurant in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRestaurantUpdatableFieldsEquals(returnedRestaurant, getPersistedRestaurant(returnedRestaurant));
    }

    @Test
    @Transactional
    void createRestaurantWithExistingId() throws Exception {
        // Create the Restaurant with an existing ID
        restaurant.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurant)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
            .andExpect(jsonPath("$.[*].restaurantName").value(hasItem(DEFAULT_RESTAURANT_NAME)))
            .andExpect(jsonPath("$.[*].restaurantDescription").value(hasItem(DEFAULT_RESTAURANT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].noOfTables").value(hasItem(DEFAULT_NO_OF_TABLES)));
    }

    @Test
    @Transactional
    void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurant.getId().intValue()))
            .andExpect(jsonPath("$.restaurantName").value(DEFAULT_RESTAURANT_NAME))
            .andExpect(jsonPath("$.restaurantDescription").value(DEFAULT_RESTAURANT_DESCRIPTION))
            .andExpect(jsonPath("$.noOfTables").value(DEFAULT_NO_OF_TABLES));
    }

    @Test
    @Transactional
    void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRestaurant are not directly saved in db
        em.detach(updatedRestaurant);
        updatedRestaurant
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .noOfTables(UPDATED_NO_OF_TABLES);

        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRestaurantToMatchAllProperties(updatedRestaurant);
    }

    @Test
    @Transactional
    void putNonExistingRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurant.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRestaurantUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRestaurant, restaurant),
            getPersistedRestaurant(restaurant)
        );
    }

    @Test
    @Transactional
    void fullUpdateRestaurantWithPatch() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurant using partial update
        Restaurant partialUpdatedRestaurant = new Restaurant();
        partialUpdatedRestaurant.setId(restaurant.getId());

        partialUpdatedRestaurant
            .restaurantName(UPDATED_RESTAURANT_NAME)
            .restaurantDescription(UPDATED_RESTAURANT_DESCRIPTION)
            .noOfTables(UPDATED_NO_OF_TABLES);

        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRestaurant))
            )
            .andExpect(status().isOk());

        // Validate the Restaurant in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRestaurantUpdatableFieldsEquals(partialUpdatedRestaurant, getPersistedRestaurant(partialUpdatedRestaurant));
    }

    @Test
    @Transactional
    void patchNonExistingRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurant.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(restaurant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurant() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(restaurant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurant in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the restaurant
        restRestaurantMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return restaurantRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Restaurant getPersistedRestaurant(Restaurant restaurant) {
        return restaurantRepository.findById(restaurant.getId()).orElseThrow();
    }

    protected void assertPersistedRestaurantToMatchAllProperties(Restaurant expectedRestaurant) {
        assertRestaurantAllPropertiesEquals(expectedRestaurant, getPersistedRestaurant(expectedRestaurant));
    }

    protected void assertPersistedRestaurantToMatchUpdatableProperties(Restaurant expectedRestaurant) {
        assertRestaurantAllUpdatablePropertiesEquals(expectedRestaurant, getPersistedRestaurant(expectedRestaurant));
    }
}
