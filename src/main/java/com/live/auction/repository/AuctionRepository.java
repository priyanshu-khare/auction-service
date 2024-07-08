package com.live.auction.repository;

import com.live.auction.entity.Auction;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Optional<Auction> findByItemId(Long itemId);

    List<Auction> findByEndDateBeforeAndStatus(Instant endDate, String status);

    List<Auction> findByItemIdIn(Set<Long> itemIds);

    /* @Query("SELECT a FROM Auction a WHERE DATE(a.endDate) = :today")
    List<Auction> findAllByEndDate(@Param("today") Instant today);*/
}
