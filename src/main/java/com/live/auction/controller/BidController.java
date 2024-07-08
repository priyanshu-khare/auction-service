package com.live.auction.controller;

import com.live.auction.auth.AuthService;
import com.live.auction.common.UserType;
import com.live.auction.dto.BidDto;
import com.live.auction.service.BidService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bid")
@Tag(name = "Bid")
public class BidController {

    private final BidService bidService;

    private final AuthService authService;

    @Autowired
    public BidController(BidService bidService, AuthService authService) {
        this.bidService = bidService;
        this.authService = authService;
    }

    @PostMapping("/place")
    @Operation(summary = "Place Bid", description = "Place a bid, action restricted to - PARTICIPANTS")
    public ResponseEntity<String> placeBid(
            @RequestHeader("Authorization") String header, @RequestBody @Valid BidDto bidDto) {
        if (!authService.validateRole(header, UserType.PARTICIPANT.name())) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        bidService.placeBid(bidDto, header);
        return ResponseEntity.ok("Bid Placed Successfully");
    }

    @GetMapping("/participant")
    @Operation(summary = "Top Participants", description = "Retrieve top participants, action restricted to - ADMIN")
    public ResponseEntity<List<Long>> getTopParticipants(
            @RequestHeader("Authorization") String header, @RequestParam int top) {
        if (!authService.validateRole(header, UserType.ADMIN.name())) {
            return ResponseEntity.status(401).build();
        }
        List<Long> participants = bidService.getTopParticipants(top);
        if (participants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(participants);
    }
}
