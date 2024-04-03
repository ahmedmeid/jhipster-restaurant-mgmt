package com.ahmedmeid.rstrntgmgt.repository;

import com.ahmedmeid.rstrntgmgt.domain.RestaurantMenu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RestaurantMenu entity.
 */
@Repository
public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Long> {
    default Optional<RestaurantMenu> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RestaurantMenu> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RestaurantMenu> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select restaurantMenu from RestaurantMenu restaurantMenu left join fetch restaurantMenu.restaurant",
        countQuery = "select count(restaurantMenu) from RestaurantMenu restaurantMenu"
    )
    Page<RestaurantMenu> findAllWithToOneRelationships(Pageable pageable);

    @Query("select restaurantMenu from RestaurantMenu restaurantMenu left join fetch restaurantMenu.restaurant")
    List<RestaurantMenu> findAllWithToOneRelationships();

    @Query(
        "select restaurantMenu from RestaurantMenu restaurantMenu left join fetch restaurantMenu.restaurant where restaurantMenu.id =:id"
    )
    Optional<RestaurantMenu> findOneWithToOneRelationships(@Param("id") Long id);
}
