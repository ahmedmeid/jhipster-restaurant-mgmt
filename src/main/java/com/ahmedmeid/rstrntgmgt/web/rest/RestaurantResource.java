package com.ahmedmeid.rstrntgmgt.web.rest;

import com.ahmedmeid.rstrntgmgt.domain.Restaurant;
import com.ahmedmeid.rstrntgmgt.repository.RestaurantRepository;
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
 * REST controller for managing {@link com.ahmedmeid.rstrntgmgt.domain.Restaurant}.
 */
@RestController
@RequestMapping("/api/restaurants")
@Transactional
public class RestaurantResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantResource.class);

    private static final String ENTITY_NAME = "restaurant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantRepository restaurantRepository;

    public RestaurantResource(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * {@code POST  /restaurants} : Create a new restaurant.
     *
     * @param restaurant the restaurant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurant, or with status {@code 400 (Bad Request)} if the restaurant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) throws URISyntaxException {
        log.debug("REST request to save Restaurant : {}", restaurant);
        if (restaurant.getId() != null) {
            throw new BadRequestAlertException("A new restaurant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        restaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.created(new URI("/api/restaurants/" + restaurant.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, restaurant.getId().toString()))
            .body(restaurant);
    }

    /**
     * {@code PUT  /restaurants/:id} : Updates an existing restaurant.
     *
     * @param id the id of the restaurant to save.
     * @param restaurant the restaurant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurant,
     * or with status {@code 400 (Bad Request)} if the restaurant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaurant restaurant
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurant : {}, {}", id, restaurant);
        if (restaurant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        restaurant = restaurantRepository.save(restaurant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurant.getId().toString()))
            .body(restaurant);
    }

    /**
     * {@code PATCH  /restaurants/:id} : Partial updates given fields of an existing restaurant, field will ignore if it is null
     *
     * @param id the id of the restaurant to save.
     * @param restaurant the restaurant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurant,
     * or with status {@code 400 (Bad Request)} if the restaurant is not valid,
     * or with status {@code 404 (Not Found)} if the restaurant is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Restaurant> partialUpdateRestaurant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaurant restaurant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurant partially : {}, {}", id, restaurant);
        if (restaurant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Restaurant> result = restaurantRepository
            .findById(restaurant.getId())
            .map(existingRestaurant -> {
                if (restaurant.getRestaurantName() != null) {
                    existingRestaurant.setRestaurantName(restaurant.getRestaurantName());
                }
                if (restaurant.getRestaurantDescription() != null) {
                    existingRestaurant.setRestaurantDescription(restaurant.getRestaurantDescription());
                }
                if (restaurant.getNoOfTables() != null) {
                    existingRestaurant.setNoOfTables(restaurant.getNoOfTables());
                }

                return existingRestaurant;
            })
            .map(restaurantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurant.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurants} : get all the restaurants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurants in body.
     */
    @GetMapping("")
    public List<Restaurant> getAllRestaurants() {
        log.debug("REST request to get all Restaurants");
        return restaurantRepository.findAll();
    }

    /**
     * {@code GET  /restaurants/:id} : get the "id" restaurant.
     *
     * @param id the id of the restaurant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable("id") Long id) {
        log.debug("REST request to get Restaurant : {}", id);
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(restaurant);
    }

    /**
     * {@code DELETE  /restaurants/:id} : delete the "id" restaurant.
     *
     * @param id the id of the restaurant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id) {
        log.debug("REST request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
