package com.live.auction.cron;

import com.live.auction.common.AuctionStatus;
import com.live.auction.entity.Auction;
import com.live.auction.entity.AuctionHistory;
import com.live.auction.entity.Item;
import com.live.auction.repository.AuctionHistoryRepository;
import com.live.auction.repository.AuctionRepository;
import com.live.auction.repository.ItemRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuctionCronService {

    private final AuctionRepository auctionRepository;

    private final ItemRepository itemRepository;

    private final AuctionHistoryRepository auctionHistoryRepository;

    private final ModelMapper mapper;

    @Autowired
    public AuctionCronService(
            AuctionRepository auctionRepository,
            ItemRepository itemRepository,
            AuctionHistoryRepository auctionHistoryRepository,
            ModelMapper mapper) {
        this.auctionRepository = auctionRepository;
        this.itemRepository = itemRepository;
        this.auctionHistoryRepository = auctionHistoryRepository;
        this.mapper = mapper;
    }

    @Scheduled(cron = "${ended.auction.status.update}")
    @Transactional
    public void updateAuctionStatus() {
        Set<Long> itemIds = new HashSet<>();
        Instant currentTime = Instant.now();
        List<Auction> auctionList =
                auctionRepository.findByEndDateBeforeAndStatus(currentTime, AuctionStatus.IN_PROGRESS.name());
        List<AuctionHistory> auctionHistoryList = new ArrayList<>();
        auctionList.forEach(auction -> {
            itemIds.add(auction.getItemId());
            if (auction.getHighestPrice().compareTo(auction.getBasePrice()) > 0) {
                auction.setStatus(AuctionStatus.SUCCESS.name());
            } else {
                auction.setStatus(AuctionStatus.FAILURE.name());
            }
            auction.setUpdatedBy(0L);
            auction.setUpdatedOn(Instant.now());
            auctionHistoryList.add(buildAuctionHistory(auction));
        });
        auctionRepository.saveAll(auctionList);
        auctionHistoryRepository.saveAll(auctionHistoryList);

        List<Auction> unPlacedAuction = new ArrayList<>();
        List<AuctionHistory> unPlacedAuctionHistory = new ArrayList<>();
        Set<Long> unPlacedItems = new HashSet<>();
        List<Item> items = itemRepository.findItemsByIdNotInAndEndDateBefore(itemIds, currentTime);
        items.forEach(item -> {
            Auction auction = buildFailureAuction(item);
            unPlacedItems.add(item.getId());
            unPlacedAuction.add(auction);
        });
        auctionRepository.saveAll(unPlacedAuction);

        List<Auction> unPlacedAuctions = auctionRepository.findByItemIdIn(unPlacedItems);
        unPlacedAuctions.forEach(auction -> unPlacedAuctionHistory.add(buildAuctionHistory(auction)));
        auctionHistoryRepository.saveAll(unPlacedAuctionHistory);
    }

    private Auction buildFailureAuction(Item item) {
        return Auction.builder()
                .basePrice(item.getBasePrice())
                .itemId(item.getId())
                .updatedBy(0L)
                .status(AuctionStatus.FAILURE.name())
                .highestPrice(item.getBasePrice())
                .endDate(item.getEndDate())
                .startDate(item.getStartDate())
                .build();
    }

    private AuctionHistory buildAuctionHistory(Auction auction) {
        return AuctionHistory.builder()
                .auctionId(auction.getId())
                .createdOn(Instant.now())
                .status(auction.getStatus())
                .highestPrice(auction.getHighestPrice())
                .basePrice(auction.getBasePrice())
                .endDate(auction.getEndDate())
                .startDate(auction.getStartDate())
                .itemId(auction.getItemId())
                .createdBy(0L)
                .build();
    }
}
