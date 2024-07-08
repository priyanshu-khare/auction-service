package com.live.auction.service.impl;

import com.live.auction.auth.AuthService;
import com.live.auction.dto.ItemDto;
import com.live.auction.entity.Item;
import com.live.auction.repository.ItemRepository;
import com.live.auction.service.ItemService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ModelMapper modelMapper;

    private final ItemRepository itemRepository;

    private final AuthService authService;

    public ItemServiceImpl(ModelMapper modelMapper, ItemRepository itemRepository, AuthService authService) {
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void saveItem(ItemDto itemDto, String header) {
        Long userId = authService.getUserId(header);
        Item item = modelMapper.map(itemDto, Item.class);
        item.setCreatedBy(userId);
        itemRepository.save(item);
    }
}
