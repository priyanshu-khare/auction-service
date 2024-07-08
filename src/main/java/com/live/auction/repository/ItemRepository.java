package com.live.auction.repository;

import com.live.auction.entity.Item;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE i.id NOT IN :itemIds AND i.endDate < :currentDate")
    List<Item> findItemsByIdNotInAndEndDateBefore(
            @Param("itemIds") Set<Long> itemIds, @Param("currentDate") Instant currentDate);
}
