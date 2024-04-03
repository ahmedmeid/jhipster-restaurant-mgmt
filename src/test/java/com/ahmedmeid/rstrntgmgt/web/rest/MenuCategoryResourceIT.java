package com.ahmedmeid.rstrntgmgt.web.rest;

import static com.ahmedmeid.rstrntgmgt.domain.MenuCategoryAsserts.*;
import static com.ahmedmeid.rstrntgmgt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ahmedmeid.rstrntgmgt.IntegrationTest;
import com.ahmedmeid.rstrntgmgt.domain.MenuCategory;
import com.ahmedmeid.rstrntgmgt.repository.MenuCategoryRepository;
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
 * Integration tests for the {@link MenuCategoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MenuCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/menu-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Mock
    private MenuCategoryRepository menuCategoryRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMenuCategoryMockMvc;

    private MenuCategory menuCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuCategory createEntity(EntityManager em) {
        MenuCategory menuCategory = new MenuCategory()
            .categoryName(DEFAULT_CATEGORY_NAME)
            .categoryDescription(DEFAULT_CATEGORY_DESCRIPTION);
        return menuCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MenuCategory createUpdatedEntity(EntityManager em) {
        MenuCategory menuCategory = new MenuCategory()
            .categoryName(UPDATED_CATEGORY_NAME)
            .categoryDescription(UPDATED_CATEGORY_DESCRIPTION);
        return menuCategory;
    }

    @BeforeEach
    public void initTest() {
        menuCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createMenuCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MenuCategory
        var returnedMenuCategory = om.readValue(
            restMenuCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuCategory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MenuCategory.class
        );

        // Validate the MenuCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMenuCategoryUpdatableFieldsEquals(returnedMenuCategory, getPersistedMenuCategory(returnedMenuCategory));
    }

    @Test
    @Transactional
    void createMenuCategoryWithExistingId() throws Exception {
        // Create the MenuCategory with an existing ID
        menuCategory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMenuCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuCategory)))
            .andExpect(status().isBadRequest());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMenuCategories() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        // Get all the menuCategoryList
        restMenuCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(menuCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryDescription").value(hasItem(DEFAULT_CATEGORY_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuCategoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(menuCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(menuCategoryRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMenuCategoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(menuCategoryRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMenuCategoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(menuCategoryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        // Get the menuCategory
        restMenuCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, menuCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(menuCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.categoryDescription").value(DEFAULT_CATEGORY_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingMenuCategory() throws Exception {
        // Get the menuCategory
        restMenuCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuCategory
        MenuCategory updatedMenuCategory = menuCategoryRepository.findById(menuCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMenuCategory are not directly saved in db
        em.detach(updatedMenuCategory);
        updatedMenuCategory.categoryName(UPDATED_CATEGORY_NAME).categoryDescription(UPDATED_CATEGORY_DESCRIPTION);

        restMenuCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMenuCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMenuCategory))
            )
            .andExpect(status().isOk());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMenuCategoryToMatchAllProperties(updatedMenuCategory);
    }

    @Test
    @Transactional
    void putNonExistingMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, menuCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(menuCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(menuCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMenuCategoryWithPatch() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuCategory using partial update
        MenuCategory partialUpdatedMenuCategory = new MenuCategory();
        partialUpdatedMenuCategory.setId(menuCategory.getId());

        partialUpdatedMenuCategory.categoryName(UPDATED_CATEGORY_NAME).categoryDescription(UPDATED_CATEGORY_DESCRIPTION);

        restMenuCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuCategory))
            )
            .andExpect(status().isOk());

        // Validate the MenuCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMenuCategory, menuCategory),
            getPersistedMenuCategory(menuCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateMenuCategoryWithPatch() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the menuCategory using partial update
        MenuCategory partialUpdatedMenuCategory = new MenuCategory();
        partialUpdatedMenuCategory.setId(menuCategory.getId());

        partialUpdatedMenuCategory.categoryName(UPDATED_CATEGORY_NAME).categoryDescription(UPDATED_CATEGORY_DESCRIPTION);

        restMenuCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMenuCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMenuCategory))
            )
            .andExpect(status().isOk());

        // Validate the MenuCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMenuCategoryUpdatableFieldsEquals(partialUpdatedMenuCategory, getPersistedMenuCategory(partialUpdatedMenuCategory));
    }

    @Test
    @Transactional
    void patchNonExistingMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, menuCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(menuCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMenuCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        menuCategory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMenuCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(menuCategory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MenuCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMenuCategory() throws Exception {
        // Initialize the database
        menuCategoryRepository.saveAndFlush(menuCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the menuCategory
        restMenuCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, menuCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return menuCategoryRepository.count();
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

    protected MenuCategory getPersistedMenuCategory(MenuCategory menuCategory) {
        return menuCategoryRepository.findById(menuCategory.getId()).orElseThrow();
    }

    protected void assertPersistedMenuCategoryToMatchAllProperties(MenuCategory expectedMenuCategory) {
        assertMenuCategoryAllPropertiesEquals(expectedMenuCategory, getPersistedMenuCategory(expectedMenuCategory));
    }

    protected void assertPersistedMenuCategoryToMatchUpdatableProperties(MenuCategory expectedMenuCategory) {
        assertMenuCategoryAllUpdatablePropertiesEquals(expectedMenuCategory, getPersistedMenuCategory(expectedMenuCategory));
    }
}
