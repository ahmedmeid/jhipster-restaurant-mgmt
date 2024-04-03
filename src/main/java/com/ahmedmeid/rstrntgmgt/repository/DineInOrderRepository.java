package com.ahmedmeid.rstrntgmgt.repository;

import com.ahmedmeid.rstrntgmgt.domain.DineInOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DineInOrder entity.
 */
@Repository
public interface DineInOrderRepository extends JpaRepository<DineInOrder, Long> {
    default Optional<DineInOrder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DineInOrder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DineInOrder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select dineInOrder from DineInOrder dineInOrder left join fetch dineInOrder.restaurant",
        countQuery = "select count(dineInOrder) from DineInOrder dineInOrder"
    )
    Page<DineInOrder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dineInOrder from DineInOrder dineInOrder left join fetch dineInOrder.restaurant")
    List<DineInOrder> findAllWithToOneRelationships();

    @Query("select dineInOrder from DineInOrder dineInOrder left join fetch dineInOrder.restaurant where dineInOrder.id =:id")
    Optional<DineInOrder> findOneWithToOneRelationships(@Param("id") Long id);
}
