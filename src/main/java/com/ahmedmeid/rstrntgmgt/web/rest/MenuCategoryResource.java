package com.ahmedmeid.rstrntgmgt.web.rest;

import com.ahmedmeid.rstrntgmgt.domain.MenuCategory;
import com.ahmedmeid.rstrntgmgt.repository.MenuCategoryRepository;
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
 * REST controller for managing {@link com.ahmedmeid.rstrntgmgt.domain.MenuCategory}.
 */
@RestController
@RequestMapping("/api/menu-categories")
@Transactional
public class MenuCategoryResource {

    private final Logger log = LoggerFactory.getLogger(MenuCategoryResource.class);

    private static final String ENTITY_NAME = "menuCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MenuCategoryRepository menuCategoryRepository;

    public MenuCategoryResource(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    /**
     * {@code POST  /menu-categories} : Create a new menuCategory.
     *
     * @param menuCategory the menuCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuCategory, or with status {@code 400 (Bad Request)} if the menuCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MenuCategory> createMenuCategory(@RequestBody MenuCategory menuCategory) throws URISyntaxException {
        log.debug("REST request to save MenuCategory : {}", menuCategory);
        if (menuCategory.getId() != null) {
            throw new BadRequestAlertException("A new menuCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        menuCategory = menuCategoryRepository.save(menuCategory);
        return ResponseEntity.created(new URI("/api/menu-categories/" + menuCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, menuCategory.getId().toString()))
            .body(menuCategory);
    }

    /**
     * {@code PUT  /menu-categories/:id} : Updates an existing menuCategory.
     *
     * @param id the id of the menuCategory to save.
     * @param menuCategory the menuCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuCategory,
     * or with status {@code 400 (Bad Request)} if the menuCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MenuCategory> updateMenuCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuCategory menuCategory
    ) throws URISyntaxException {
        log.debug("REST request to update MenuCategory : {}, {}", id, menuCategory);
        if (menuCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        menuCategory = menuCategoryRepository.save(menuCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuCategory.getId().toString()))
            .body(menuCategory);
    }

    /**
     * {@code PATCH  /menu-categories/:id} : Partial updates given fields of an existing menuCategory, field will ignore if it is null
     *
     * @param id the id of the menuCategory to save.
     * @param menuCategory the menuCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuCategory,
     * or with status {@code 400 (Bad Request)} if the menuCategory is not valid,
     * or with status {@code 404 (Not Found)} if the menuCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MenuCategory> partialUpdateMenuCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MenuCategory menuCategory
    ) throws URISyntaxException {
        log.debug("REST request to partial update MenuCategory partially : {}, {}", id, menuCategory);
        if (menuCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MenuCategory> result = menuCategoryRepository
            .findById(menuCategory.getId())
            .map(existingMenuCategory -> {
                if (menuCategory.getCategoryName() != null) {
                    existingMenuCategory.setCategoryName(menuCategory.getCategoryName());
                }
                if (menuCategory.getCategoryDescription() != null) {
                    existingMenuCategory.setCategoryDescription(menuCategory.getCategoryDescription());
                }

                return existingMenuCategory;
            })
            .map(menuCategoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, menuCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /menu-categories} : get all the menuCategories.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuCategories in body.
     */
    @GetMapping("")
    public List<MenuCategory> getAllMenuCategories(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all MenuCategories");
        if (eagerload) {
            return menuCategoryRepository.findAllWithEagerRelationships();
        } else {
            return menuCategoryRepository.findAll();
        }
    }

    /**
     * {@code GET  /menu-categories/:id} : get the "id" menuCategory.
     *
     * @param id the id of the menuCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuCategory> getMenuCategory(@PathVariable("id") Long id) {
        log.debug("REST request to get MenuCategory : {}", id);
        Optional<MenuCategory> menuCategory = menuCategoryRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(menuCategory);
    }

    /**
     * {@code DELETE  /menu-categories/:id} : delete the "id" menuCategory.
     *
     * @param id the id of the menuCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuCategory(@PathVariable("id") Long id) {
        log.debug("REST request to delete MenuCategory : {}", id);
        menuCategoryRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
