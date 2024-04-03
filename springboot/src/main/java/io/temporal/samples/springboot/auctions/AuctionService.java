package io.temporal.samples.springboot.auctions;

import io.temporal.activity.ActivityInterface;
import java.util.UUID;

@ActivityInterface
public interface AuctionService {

  Auction startAuction(UUID auctionId, Long carId);

  Auction endAuction(UUID auctionId);

  BidStatus makeBid(UUID auctionId, String bidder, Long bidValue);
}
