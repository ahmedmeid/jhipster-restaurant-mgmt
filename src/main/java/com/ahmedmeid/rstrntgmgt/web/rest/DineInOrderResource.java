package com.ahmedmeid.rstrntgmgt.web.rest;

import com.ahmedmeid.rstrntgmgt.domain.DineInOrder;
import com.ahmedmeid.rstrntgmgt.repository.DineInOrderRepository;
import com.ahmedmeid.rstrntgmgt.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ahmedmeid.rstrntgmgt.domain.DineInOrder}.
 */
@RestController
@RequestMapping("/api/dine-in-orders")
@Transactional
public class DineInOrderResource {

    private final Logger log = LoggerFactory.getLogger(DineInOrderResource.class);

    private static final String ENTITY_NAME = "dineInOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DineInOrderRepository dineInOrderRepository;

    public DineInOrderResource(DineInOrderRepository dineInOrderRepository) {
        this.dineInOrderRepository = dineInOrderRepository;
    }

    /**
     * {@code POST  /dine-in-orders} : Create a new dineInOrder.
     *
     * @param dineInOrder the dineInOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dineInOrder, or with status {@code 400 (Bad Request)} if the dineInOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DineInOrder> createDineInOrder(@RequestBody DineInOrder dineInOrder) throws URISyntaxException {
        log.debug("REST request to save DineInOrder : {}", dineInOrder);
        if (dineInOrder.getId() != null) {
            throw new BadRequestAlertException("A new dineInOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dineInOrder = dineInOrderRepository.save(dineInOrder);
        return ResponseEntity.created(new URI("/api/dine-in-orders/" + dineInOrder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dineInOrder.getId().toString()))
            .body(dineInOrder);
    }

    /**
     * {@code PUT  /dine-in-orders/:id} : Updates an existing dineInOrder.
     *
     * @param id the id of the dineInOrder to save.
     * @param dineInOrder the dineInOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dineInOrder,
     * or with status {@code 400 (Bad Request)} if the dineInOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dineInOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DineInOrder> updateDineInOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DineInOrder dineInOrder
    ) throws URISyntaxException {
        log.debug("REST request to update DineInOrder : {}, {}", id, dineInOrder);
        if (dineInOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dineInOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dineInOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dineInOrder = dineInOrderRepository.save(dineInOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dineInOrder.getId().toString()))
            .body(dineInOrder);
    }

    /**
     * {@code PATCH  /dine-in-orders/:id} : Partial updates given fields of an existing dineInOrder, field will ignore if it is null
     *
     * @param id the id of the dineInOrder to save.
     * @param dineInOrder the dineInOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dineInOrder,
     * or with status {@code 400 (Bad Request)} if the dineInOrder is not valid,
     * or with status {@code 404 (Not Found)} if the dineInOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the dineInOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DineInOrder> partialUpdateDineInOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DineInOrder dineInOrder
    ) throws URISyntaxException {
        log.debug("REST request to partial update DineInOrder partially : {}, {}", id, dineInOrder);
        if (dineInOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dineInOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dineInOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DineInOrder> result = dineInOrderRepository
            .findById(dineInOrder.getId())
            .map(existingDineInOrder -> {
                if (dineInOrder.getOrderTime() != null) {
                    existingDineInOrder.setOrderTime(dineInOrder.getOrderTime());
                }
                if (dineInOrder.getTableNumber() != null) {
                    existingDineInOrder.setTableNumber(dineInOrder.getTableNumber());
                }

                return existingDineInOrder;
            })
            .map(dineInOrderRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dineInOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /dine-in-orders} : get all the dineInOrders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dineInOrders in body.
     */
    @GetMapping("")
    public List<DineInOrder> getAllDineInOrders(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all DineInOrders");
        if (eagerload) {
            return dineInOrderRepository.findAllWithEagerRelationships();
        } else {
            return dineInOrderRepository.findAll();
        }
    }

    /**
     * {@code GET  /dine-in-orders/:id} : get the "id" dineInOrder.
     *
     * @param id the id of the dineInOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dineInOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DineInOrder> getDineInOrder(@PathVariable("id") Long id) {
        log.debug("REST request to get DineInOrder : {}", id);
        Optional<DineInOrder> dineInOrder = dineInOrderRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(dineInOrder);
    }

    /**
     * {@code DELETE  /dine-in-orders/:id} : delete the "id" dineInOrder.
     *
     * @param id the id of the dineInOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDineInOrder(@PathVariable("id") Long id) {
        log.debug("REST request to delete DineInOrder : {}", id);
        dineInOrderRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
