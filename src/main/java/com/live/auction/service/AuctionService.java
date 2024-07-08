package com.live.auction.service;

import com.live.auction.dto.AuctionDto;
import com.live.auction.dto.AuctionItemDto;
import com.live.auction.entity.Auction;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface AuctionService {

    void saveOrUpdateByItemId(Auction auction);

    List<AuctionItemDto> getAuctionItems();

    List<Auction> getAllAuctionItems();

    List<Long> getTopAuctioneers(int top);

    Optional<AuctionDto> findByItemId(long id);
}
