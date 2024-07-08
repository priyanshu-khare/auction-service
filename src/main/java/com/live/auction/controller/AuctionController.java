package com.live.auction.controller;

import com.live.auction.auth.AuthService;
import com.live.auction.common.UserType;
import com.live.auction.dto.AuctionDto;
import com.live.auction.dto.AuctionItemDto;
import com.live.auction.entity.Auction;
import com.live.auction.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")
@Tag(name = "Auction")
public class AuctionController {

    private final AuctionService auctionService;

    private final AuthService authService;

    @Autowired
    public AuctionController(AuctionService auctionService, AuthService authService) {
        this.auctionService = auctionService;
        this.authService = authService;
    }

    @GetMapping("/live")
    @Operation(summary = "Live Auction Items", description = "Retrieve live auction items")
    public ResponseEntity<List<AuctionItemDto>> getAuctionItems() {
        List<AuctionItemDto> auctionItems = auctionService.getAuctionItems();
        if (auctionItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auctionItems);
    }

    @GetMapping("/all")
    @Operation(summary = "All Auction Items", description = "Retrieve all auction items, action restricted to - ADMIN")
    public ResponseEntity<List<Auction>> getAllAuctionItems(@RequestHeader("Authorization") String header) {
        if (!authService.validateRole(header, UserType.ADMIN.name())) {
            return ResponseEntity.status(401).build();
        }
        List<Auction> auctionItems = auctionService.getAllAuctionItems();
        if (auctionItems.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auctionItems);
    }

    @GetMapping("/auctioneer")
    @Operation(
            summary = "Top Auctioneers",
            description = "Retrieve ids of top auctioneers, action restricted to - ADMIN")
    public ResponseEntity<List<Long>> getTopAuctioneers(
            @RequestHeader("Authorization") String header, @RequestParam int top) {
        if (!authService.validateRole(header, UserType.ADMIN.name())) {
            return ResponseEntity.status(401).build();
        }
        List<Long> auctioneers = auctionService.getTopAuctioneers(top);
        if (auctioneers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auctioneers);
    }

    @GetMapping
    @Operation(summary = "Get Auction", description = "Retrieve auction by Item Id, action restricted to - AUCTIONEER")
    public ResponseEntity<Optional<AuctionDto>> findByItemId(
            @RequestHeader("Authorization") String header, @RequestParam long itemId) {
        if (!authService.validateRole(header, UserType.AUCTIONEER.name())) {
            return ResponseEntity.status(401).build();
        }
        Optional<AuctionDto> auction = auctionService.findByItemId(itemId);
        if (auction.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(auction);
    }
}
