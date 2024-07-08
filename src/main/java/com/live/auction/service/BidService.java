package com.live.auction.service;

import com.live.auction.dto.BidDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface BidService {

    void placeBid(BidDto bidDto, String header);

    List<Long> getTopParticipants(int top);
}
