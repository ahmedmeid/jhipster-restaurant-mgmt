package com.ahmedmeid.rstrntgmgt.web.rest;

import com.ahmedmeid.rstrntgmgt.domain.RestaurantMenu;
import com.ahmedmeid.rstrntgmgt.repository.RestaurantMenuRepository;
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
 * REST controller for managing {@link com.ahmedmeid.rstrntgmgt.domain.RestaurantMenu}.
 */
@RestController
@RequestMapping("/api/restaurant-menus")
@Transactional
public class RestaurantMenuResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantMenuResource.class);

    private static final String ENTITY_NAME = "restaurantMenu";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantMenuRepository restaurantMenuRepository;

    public RestaurantMenuResource(RestaurantMenuRepository restaurantMenuRepository) {
        this.restaurantMenuRepository = restaurantMenuRepository;
    }

    /**
     * {@code POST  /restaurant-menus} : Create a new restaurantMenu.
     *
     * @param restaurantMenu the restaurantMenu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurantMenu, or with status {@code 400 (Bad Request)} if the restaurantMenu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RestaurantMenu> createRestaurantMenu(@RequestBody RestaurantMenu restaurantMenu) throws URISyntaxException {
        log.debug("REST request to save RestaurantMenu : {}", restaurantMenu);
        if (restaurantMenu.getId() != null) {
            throw new BadRequestAlertException("A new restaurantMenu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        restaurantMenu = restaurantMenuRepository.save(restaurantMenu);
        return ResponseEntity.created(new URI("/api/restaurant-menus/" + restaurantMenu.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, restaurantMenu.getId().toString()))
            .body(restaurantMenu);
    }

    /**
     * {@code PUT  /restaurant-menus/:id} : Updates an existing restaurantMenu.
     *
     * @param id the id of the restaurantMenu to save.
     * @param restaurantMenu the restaurantMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantMenu,
     * or with status {@code 400 (Bad Request)} if the restaurantMenu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurantMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantMenu> updateRestaurantMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantMenu restaurantMenu
    ) throws URISyntaxException {
        log.debug("REST request to update RestaurantMenu : {}, {}", id, restaurantMenu);
        if (restaurantMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        restaurantMenu = restaurantMenuRepository.save(restaurantMenu);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantMenu.getId().toString()))
            .body(restaurantMenu);
    }

    /**
     * {@code PATCH  /restaurant-menus/:id} : Partial updates given fields of an existing restaurantMenu, field will ignore if it is null
     *
     * @param id the id of the restaurantMenu to save.
     * @param restaurantMenu the restaurantMenu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurantMenu,
     * or with status {@code 400 (Bad Request)} if the restaurantMenu is not valid,
     * or with status {@code 404 (Not Found)} if the restaurantMenu is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurantMenu couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RestaurantMenu> partialUpdateRestaurantMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RestaurantMenu restaurantMenu
    ) throws URISyntaxException {
        log.debug("REST request to partial update RestaurantMenu partially : {}, {}", id, restaurantMenu);
        if (restaurantMenu.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurantMenu.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantMenuRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RestaurantMenu> result = restaurantMenuRepository
            .findById(restaurantMenu.getId())
            .map(existingRestaurantMenu -> {
                if (restaurantMenu.getMenuName() != null) {
                    existingRestaurantMenu.setMenuName(restaurantMenu.getMenuName());
                }
                if (restaurantMenu.getMenuDescription() != null) {
                    existingRestaurantMenu.setMenuDescription(restaurantMenu.getMenuDescription());
                }

                return existingRestaurantMenu;
            })
            .map(restaurantMenuRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurantMenu.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurant-menus} : get all the restaurantMenus.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurantMenus in body.
     */
    @GetMapping("")
    public List<RestaurantMenu> getAllRestaurantMenus(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all RestaurantMenus");
        if (eagerload) {
            return restaurantMenuRepository.findAllWithEagerRelationships();
        } else {
            return restaurantMenuRepository.findAll();
        }
    }

    /**
     * {@code GET  /restaurant-menus/:id} : get the "id" restaurantMenu.
     *
     * @param id the id of the restaurantMenu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurantMenu, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantMenu> getRestaurantMenu(@PathVariable("id") Long id) {
        log.debug("REST request to get RestaurantMenu : {}", id);
        Optional<RestaurantMenu> restaurantMenu = restaurantMenuRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(restaurantMenu);
    }

    /**
     * {@code DELETE  /restaurant-menus/:id} : delete the "id" restaurantMenu.
     *
     * @param id the id of the restaurantMenu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurantMenu(@PathVariable("id") Long id) {
        log.debug("REST request to delete RestaurantMenu : {}", id);
        restaurantMenuRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
