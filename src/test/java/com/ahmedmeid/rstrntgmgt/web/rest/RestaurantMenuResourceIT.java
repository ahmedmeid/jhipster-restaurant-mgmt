package com.ahmedmeid.rstrntgmgt.web.rest;

import static com.ahmedmeid.rstrntgmgt.domain.RestaurantMenuAsserts.*;
import static com.ahmedmeid.rstrntgmgt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ahmedmeid.rstrntgmgt.IntegrationTest;
import com.ahmedmeid.rstrntgmgt.domain.RestaurantMenu;
import com.ahmedmeid.rstrntgmgt.repository.RestaurantMenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestaurantMenuResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurantMenuResourceIT {

    private static final String DEFAULT_MENU_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MENU_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MENU_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MENU_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurant-menus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RestaurantMenuRepository restaurantMenuRepository;

    @Mock
    private RestaurantMenuRepository restaurantMenuRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurantMenuMockMvc;

    private RestaurantMenu restaurantMenu;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenu createEntity(EntityManager em) {
        RestaurantMenu restaurantMenu = new RestaurantMenu().menuName(DEFAULT_MENU_NAME).menuDescription(DEFAULT_MENU_DESCRIPTION);
        return restaurantMenu;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RestaurantMenu createUpdatedEntity(EntityManager em) {
        RestaurantMenu restaurantMenu = new RestaurantMenu().menuName(UPDATED_MENU_NAME).menuDescription(UPDATED_MENU_DESCRIPTION);
        return restaurantMenu;
    }

    @BeforeEach
    public void initTest() {
        restaurantMenu = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurantMenu() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RestaurantMenu
        var returnedRestaurantMenu = om.readValue(
            restRestaurantMenuMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurantMenu)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RestaurantMenu.class
        );

        // Validate the RestaurantMenu in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRestaurantMenuUpdatableFieldsEquals(returnedRestaurantMenu, getPersistedRestaurantMenu(returnedRestaurantMenu));
    }

    @Test
    @Transactional
    void createRestaurantMenuWithExistingId() throws Exception {
        // Create the RestaurantMenu with an existing ID
        restaurantMenu.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMenuMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurantMenu)))
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurantMenus() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get all the restaurantMenuList
        restRestaurantMenuMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurantMenu.getId().intValue())))
            .andExpect(jsonPath("$.[*].menuName").value(hasItem(DEFAULT_MENU_NAME)))
            .andExpect(jsonPath("$.[*].menuDescription").value(hasItem(DEFAULT_MENU_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRestaurantMenusWithEagerRelationshipsIsEnabled() throws Exception {
        when(restaurantMenuRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRestaurantMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(restaurantMenuRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRestaurantMenusWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(restaurantMenuRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRestaurantMenuMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(restaurantMenuRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        // Get the restaurantMenu
        restRestaurantMenuMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurantMenu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurantMenu.getId().intValue()))
            .andExpect(jsonPath("$.menuName").value(DEFAULT_MENU_NAME))
            .andExpect(jsonPath("$.menuDescription").value(DEFAULT_MENU_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRestaurantMenu() throws Exception {
        // Get the restaurantMenu
        restRestaurantMenuMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurantMenu
        RestaurantMenu updatedRestaurantMenu = restaurantMenuRepository.findById(restaurantMenu.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRestaurantMenu are not directly saved in db
        em.detach(updatedRestaurantMenu);
        updatedRestaurantMenu.menuName(UPDATED_MENU_NAME).menuDescription(UPDATED_MENU_DESCRIPTION);

        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurantMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRestaurantMenuToMatchAllProperties(updatedRestaurantMenu);
    }

    @Test
    @Transactional
    void putNonExistingRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurantMenu.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(restaurantMenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurantMenuWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurantMenu using partial update
        RestaurantMenu partialUpdatedRestaurantMenu = new RestaurantMenu();
        partialUpdatedRestaurantMenu.setId(restaurantMenu.getId());

        partialUpdatedRestaurantMenu.menuName(UPDATED_MENU_NAME);

        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRestaurantMenuUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRestaurantMenu, restaurantMenu),
            getPersistedRestaurantMenu(restaurantMenu)
        );
    }

    @Test
    @Transactional
    void fullUpdateRestaurantMenuWithPatch() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the restaurantMenu using partial update
        RestaurantMenu partialUpdatedRestaurantMenu = new RestaurantMenu();
        partialUpdatedRestaurantMenu.setId(restaurantMenu.getId());

        partialUpdatedRestaurantMenu.menuName(UPDATED_MENU_NAME).menuDescription(UPDATED_MENU_DESCRIPTION);

        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRestaurantMenu))
            )
            .andExpect(status().isOk());

        // Validate the RestaurantMenu in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRestaurantMenuUpdatableFieldsEquals(partialUpdatedRestaurantMenu, getPersistedRestaurantMenu(partialUpdatedRestaurantMenu));
    }

    @Test
    @Transactional
    void patchNonExistingRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurantMenu.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(restaurantMenu))
            )
            .andExpect(status().isBadRequest());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurantMenu() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        restaurantMenu.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurantMenuMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(restaurantMenu)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RestaurantMenu in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurantMenu() throws Exception {
        // Initialize the database
        restaurantMenuRepository.saveAndFlush(restaurantMenu);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the restaurantMenu
        restRestaurantMenuMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurantMenu.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return restaurantMenuRepository.count();
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

    protected RestaurantMenu getPersistedRestaurantMenu(RestaurantMenu restaurantMenu) {
        return restaurantMenuRepository.findById(restaurantMenu.getId()).orElseThrow();
    }

    protected void assertPersistedRestaurantMenuToMatchAllProperties(RestaurantMenu expectedRestaurantMenu) {
        assertRestaurantMenuAllPropertiesEquals(expectedRestaurantMenu, getPersistedRestaurantMenu(expectedRestaurantMenu));
    }

    protected void assertPersistedRestaurantMenuToMatchUpdatableProperties(RestaurantMenu expectedRestaurantMenu) {
        assertRestaurantMenuAllUpdatablePropertiesEquals(expectedRestaurantMenu, getPersistedRestaurantMenu(expectedRestaurantMenu));
    }
}
