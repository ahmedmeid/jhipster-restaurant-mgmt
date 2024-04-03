package com.ahmedmeid.rstrntgmgt.web.rest;

import static com.ahmedmeid.rstrntgmgt.domain.DineInOrderAsserts.*;
import static com.ahmedmeid.rstrntgmgt.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ahmedmeid.rstrntgmgt.IntegrationTest;
import com.ahmedmeid.rstrntgmgt.domain.DineInOrder;
import com.ahmedmeid.rstrntgmgt.repository.DineInOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DineInOrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DineInOrderResourceIT {

    private static final Instant DEFAULT_ORDER_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TABLE_NUMBER = 1;
    private static final Integer UPDATED_TABLE_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/dine-in-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DineInOrderRepository dineInOrderRepository;

    @Mock
    private DineInOrderRepository dineInOrderRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDineInOrderMockMvc;

    private DineInOrder dineInOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DineInOrder createEntity(EntityManager em) {
        DineInOrder dineInOrder = new DineInOrder().orderTime(DEFAULT_ORDER_TIME).tableNumber(DEFAULT_TABLE_NUMBER);
        return dineInOrder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DineInOrder createUpdatedEntity(EntityManager em) {
        DineInOrder dineInOrder = new DineInOrder().orderTime(UPDATED_ORDER_TIME).tableNumber(UPDATED_TABLE_NUMBER);
        return dineInOrder;
    }

    @BeforeEach
    public void initTest() {
        dineInOrder = createEntity(em);
    }

    @Test
    @Transactional
    void createDineInOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DineInOrder
        var returnedDineInOrder = om.readValue(
            restDineInOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dineInOrder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DineInOrder.class
        );

        // Validate the DineInOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDineInOrderUpdatableFieldsEquals(returnedDineInOrder, getPersistedDineInOrder(returnedDineInOrder));
    }

    @Test
    @Transactional
    void createDineInOrderWithExistingId() throws Exception {
        // Create the DineInOrder with an existing ID
        dineInOrder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDineInOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dineInOrder)))
            .andExpect(status().isBadRequest());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDineInOrders() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        // Get all the dineInOrderList
        restDineInOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dineInOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderTime").value(hasItem(DEFAULT_ORDER_TIME.toString())))
            .andExpect(jsonPath("$.[*].tableNumber").value(hasItem(DEFAULT_TABLE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDineInOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(dineInOrderRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDineInOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(dineInOrderRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDineInOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(dineInOrderRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDineInOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(dineInOrderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDineInOrder() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        // Get the dineInOrder
        restDineInOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, dineInOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dineInOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderTime").value(DEFAULT_ORDER_TIME.toString()))
            .andExpect(jsonPath("$.tableNumber").value(DEFAULT_TABLE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingDineInOrder() throws Exception {
        // Get the dineInOrder
        restDineInOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDineInOrder() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dineInOrder
        DineInOrder updatedDineInOrder = dineInOrderRepository.findById(dineInOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDineInOrder are not directly saved in db
        em.detach(updatedDineInOrder);
        updatedDineInOrder.orderTime(UPDATED_ORDER_TIME).tableNumber(UPDATED_TABLE_NUMBER);

        restDineInOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDineInOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDineInOrder))
            )
            .andExpect(status().isOk());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDineInOrderToMatchAllProperties(updatedDineInOrder);
    }

    @Test
    @Transactional
    void putNonExistingDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dineInOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dineInOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dineInOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dineInOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDineInOrderWithPatch() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dineInOrder using partial update
        DineInOrder partialUpdatedDineInOrder = new DineInOrder();
        partialUpdatedDineInOrder.setId(dineInOrder.getId());

        restDineInOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDineInOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDineInOrder))
            )
            .andExpect(status().isOk());

        // Validate the DineInOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDineInOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDineInOrder, dineInOrder),
            getPersistedDineInOrder(dineInOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdateDineInOrderWithPatch() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dineInOrder using partial update
        DineInOrder partialUpdatedDineInOrder = new DineInOrder();
        partialUpdatedDineInOrder.setId(dineInOrder.getId());

        partialUpdatedDineInOrder.orderTime(UPDATED_ORDER_TIME).tableNumber(UPDATED_TABLE_NUMBER);

        restDineInOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDineInOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDineInOrder))
            )
            .andExpect(status().isOk());

        // Validate the DineInOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDineInOrderUpdatableFieldsEquals(partialUpdatedDineInOrder, getPersistedDineInOrder(partialUpdatedDineInOrder));
    }

    @Test
    @Transactional
    void patchNonExistingDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dineInOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dineInOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dineInOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDineInOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dineInOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDineInOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dineInOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DineInOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDineInOrder() throws Exception {
        // Initialize the database
        dineInOrderRepository.saveAndFlush(dineInOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dineInOrder
        restDineInOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, dineInOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dineInOrderRepository.count();
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

    protected DineInOrder getPersistedDineInOrder(DineInOrder dineInOrder) {
        return dineInOrderRepository.findById(dineInOrder.getId()).orElseThrow();
    }

    protected void assertPersistedDineInOrderToMatchAllProperties(DineInOrder expectedDineInOrder) {
        assertDineInOrderAllPropertiesEquals(expectedDineInOrder, getPersistedDineInOrder(expectedDineInOrder));
    }

    protected void assertPersistedDineInOrderToMatchUpdatableProperties(DineInOrder expectedDineInOrder) {
        assertDineInOrderAllUpdatablePropertiesEquals(expectedDineInOrder, getPersistedDineInOrder(expectedDineInOrder));
    }
}
