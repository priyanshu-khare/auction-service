package com.live.auction.controller;

import com.live.auction.auth.AuthService;
import com.live.auction.common.UserType;
import com.live.auction.dto.ItemDto;
import com.live.auction.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@Tag(name = "Item")
public class ItemController {

    private final ItemService itemService;

    private final AuthService authService;

    @Autowired
    public ItemController(ItemService itemService, AuthService authService) {
        this.itemService = itemService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Save Auction Items", description = "Save auction items, action restricted to - AUCTIONEER")
    public ResponseEntity<String> saveItem(
            @RequestHeader("Authorization") String header, @RequestBody @Valid ItemDto itemDto) {
        if (!authService.validateRole(header, UserType.AUCTIONEER.name())) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        itemService.saveItem(itemDto, header);
        return ResponseEntity.ok("Item Added Successfully");
    }
}
