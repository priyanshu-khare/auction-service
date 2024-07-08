package com.live.auction.service;

import com.live.auction.dto.ItemDto;
import org.springframework.stereotype.Service;

@Service
public interface ItemService {

    void saveItem(ItemDto itemDto, String header);
}
