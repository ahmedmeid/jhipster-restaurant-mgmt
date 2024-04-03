package com.ahmedmeid.rstrntgmgt.repository;

import com.ahmedmeid.rstrntgmgt.domain.MenuCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MenuCategory entity.
 */
@Repository
public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    default Optional<MenuCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MenuCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MenuCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select menuCategory from MenuCategory menuCategory left join fetch menuCategory.menu",
        countQuery = "select count(menuCategory) from MenuCategory menuCategory"
    )
    Page<MenuCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select menuCategory from MenuCategory menuCategory left join fetch menuCategory.menu")
    List<MenuCategory> findAllWithToOneRelationships();

    @Query("select menuCategory from MenuCategory menuCategory left join fetch menuCategory.menu where menuCategory.id =:id")
    Optional<MenuCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
