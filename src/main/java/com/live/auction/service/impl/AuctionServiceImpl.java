package com.live.auction.service.impl;

import com.live.auction.common.AuctionStatus;
import com.live.auction.dto.AuctionDto;
import com.live.auction.dto.AuctionHistoryDto;
import com.live.auction.dto.AuctionItemDto;
import com.live.auction.entity.Auction;
import com.live.auction.entity.AuctionHistory;
import com.live.auction.entity.Item;
import com.live.auction.repository.AuctionHistoryRepository;
import com.live.auction.repository.AuctionRepository;
import com.live.auction.service.AuctionService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;

    private final AuctionHistoryRepository auctionHistoryRepository;

    private final EntityManager entityManager;

    private final ModelMapper mapper;

    @Autowired
    public AuctionServiceImpl(
            AuctionRepository auctionRepository,
            AuctionHistoryRepository auctionHistoryRepository,
            EntityManager entityManager,
            ModelMapper mapper) {
        this.auctionRepository = auctionRepository;
        this.auctionHistoryRepository = auctionHistoryRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void saveOrUpdateByItemId(Auction auction) {
        Optional<Auction> auctionOptional = auctionRepository.findByItemId(auction.getItemId());
        if (auctionOptional.isPresent()) {
            Auction existing = auctionOptional.get();
            auction.setId(existing.getId());
        }
        auction.setStatus(AuctionStatus.IN_PROGRESS.name());
        auctionRepository.save(auction);
        updateAuctionHistory(auction);
    }

    @Transactional
    private void updateAuctionHistory(Auction auction) {
        Optional<Auction> updatedAuction = auctionRepository.findByItemId(auction.getItemId());
        if (updatedAuction.isPresent()) {
            Auction updated = updatedAuction.get();
            AuctionHistoryDto auctionHistoryDto = mapper.map(updated, AuctionHistoryDto.class);
            AuctionHistory auctionHistory = mapper.map(auctionHistoryDto, AuctionHistory.class);
            auctionHistory.setAuctionId(updated.getId());
            auctionHistory.setCreatedBy(updated.getUpdatedBy());
            auctionHistoryRepository.save(auctionHistory);
        }
    }

    @Override
    public List<AuctionItemDto> getAuctionItems() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuctionItemDto> query = cb.createQuery(AuctionItemDto.class);
        Root<Item> itemRoot = query.from(Item.class);
        Join<Item, Auction> auctionJoin = itemRoot.join("auctionList", JoinType.LEFT);
        Instant currentTime = Instant.now();
        Predicate startDatePredicate = cb.lessThanOrEqualTo(itemRoot.get("startDate"), currentTime);
        Predicate endDatePredicate = cb.greaterThanOrEqualTo(itemRoot.get("endDate"), currentTime);
        Predicate dateRangePredicate = cb.and(startDatePredicate, endDatePredicate);

        query.select(cb.construct(
                        AuctionItemDto.class,
                        itemRoot.get("id"),
                        itemRoot.get("startDate"),
                        itemRoot.get("endDate"),
                        itemRoot.get("basePrice"),
                        cb.coalesce(auctionJoin.get("highestPrice"), itemRoot.get("basePrice"))))
                .distinct(true)
                .where(dateRangePredicate);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Auction> getAllAuctionItems() {
        return auctionRepository.findAll();
    }

    @Override
    public List<Long> getTopAuctioneers(int top) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Item> item = query.from(Item.class);

        query.select(item.get("createdBy"))
                .groupBy(item.get("createdBy"))
                .orderBy(cb.desc(cb.count(item)))
                .having(cb.gt(cb.count(item), 0));

        return entityManager.createQuery(query).setMaxResults(top).getResultList();
    }

    @Override
    public Optional<AuctionDto> findByItemId(long id) {
        Optional<Auction> auction = auctionRepository.findByItemId(id);
        return auction.map(value -> mapper.map(value, AuctionDto.class));
    }
}
