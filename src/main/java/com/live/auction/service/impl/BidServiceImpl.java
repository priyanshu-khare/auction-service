package com.live.auction.service.impl;

import com.live.auction.auth.AuthService;
import com.live.auction.dto.BidDto;
import com.live.auction.entity.Auction;
import com.live.auction.entity.Bid;
import com.live.auction.repository.BidRepository;
import com.live.auction.service.AuctionService;
import com.live.auction.service.BidService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;

    private final ModelMapper mapper;

    private final AuctionService auctionService;

    private final EntityManager entityManager;

    private final AuthService authService;

    @Autowired
    public BidServiceImpl(
            BidRepository bidRepository,
            ModelMapper mapper,
            AuctionService auctionService,
            EntityManager entityManager,
            AuthService authService) {
        this.bidRepository = bidRepository;
        this.mapper = mapper;
        this.auctionService = auctionService;
        this.entityManager = entityManager;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void placeBid(BidDto bidDto, String header) {
        Bid bid = mapper.map(bidDto, Bid.class);
        long userId = authService.getUserId(header);
        bid.setCreatedBy(userId);
        bid.setCreatedOn(Instant.now());
        bidRepository.save(bid);
        Auction auction = Auction.builder()
                .itemId(bid.getItemId())
                .highestPrice(bid.getPrice())
                .basePrice(bidDto.getBasePrice())
                .startDate(bidDto.getStartDate())
                .endDate(bidDto.getEndDate())
                .updatedBy(userId)
                .build();
        auctionService.saveOrUpdateByItemId(auction);
    }

    @Override
    public List<Long> getTopParticipants(int top) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Bid> bid = query.from(Bid.class);

        query.select(bid.get("createdBy"))
                .groupBy(bid.get("createdBy"))
                .orderBy(cb.desc(cb.count(bid)))
                .having(cb.gt(cb.count(bid), 0));

        return entityManager.createQuery(query).setMaxResults(top).getResultList();
    }
}
