package io.temporal.samples.springboot.auctions;

import io.temporal.spring.boot.ActivityImpl;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = "AuctionTaskQueue")
public class AuctionServiceImpl implements AuctionService {

  private static final Logger logger = LoggerFactory.getLogger(AuctionServiceImpl.class);
  private static final int DEFAULT_AUCTION_LENGTH = 1;
  private static final int AUCTION_LENGTH_EXTENSION = 1;

  private final AuctionRepository auctionRepository;

  public AuctionServiceImpl(AuctionRepository auctionRepository) {
    this.auctionRepository = auctionRepository;
  }

  @Override
  public Auction startAuction(UUID auctionId, Long carId) {
    logger.info("Starting auction {} for car {}", auctionId, carId);
    LocalDateTime startTime = LocalDateTime.now();
    LocalDateTime expectedEndDate = startTime.plusMinutes(DEFAULT_AUCTION_LENGTH);

    Auction auction = new Auction();
    auction.setId(auctionId);
    auction.setCarId(carId);
    auction.setStartTime(startTime);
    auction.setExpectedEndTime(expectedEndDate);
    auction.setStatus(AuctionStatus.RUNNING);

    return auctionRepository.save(auction);
  }

  @Override
  public Auction endAuction(UUID auctionId) {
    Auction auction = auctionRepository.findById(auctionId).get();
    if (isAfterExpectedEndTime(auction)) {
      logger.info("Ending auction {} for car {}", auctionId, auction.getCarId());

      auction.setStatus(AuctionStatus.ENDED);
      return auctionRepository.save(auction);
    } else {
      logger.info(
          "Not ending auction {} for car {}, expectedEndTime: {}",
          auctionId,
          auction.getCarId(),
          auction.getExpectedEndTime());
      return auction;
    }
  }

  @Override
  public BidStatus makeBid(UUID auctionId, String bidder, Long bidValue) {
    logger.info("Trying to make a bid {} on auction {}", bidValue, auctionId);
    // todo idempotency
    Auction auction = auctionRepository.findById(auctionId).get();
    if (canMakeABid(bidValue, auction)) {
      logger.info("Bid {} to auction {} rejected", bidValue, auction.getId());
      return BidStatus.REJECTED;
    } else {
      sleep();
      auction.setHighestBid(bidValue);
      auction.setLeadingBidder(bidder);

      LocalDateTime newExpectedEndTime =
          auction.getExpectedEndTime().plusMinutes(AUCTION_LENGTH_EXTENSION);
      auction.setExpectedEndTime(newExpectedEndTime);

      auctionRepository.save(auction);
      logger.info(
          "Bid {} made to auction {}. ExpectedEndTime extended: {}",
          bidValue,
          auction.getId(),
          newExpectedEndTime);
      return BidStatus.MADE;
    }
  }

  private void sleep() {
    try {
      Thread.sleep(new Random().nextInt(1000));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean canMakeABid(Long bidValue, Auction auction) {
    return isAfterExpectedEndTime(auction)
        || auction.getStatus() != AuctionStatus.RUNNING
        || auction.getHighestBid() >= bidValue;
  }

  private boolean isAfterExpectedEndTime(Auction auction) {
    return LocalDateTime.now().isAfter(auction.getExpectedEndTime());
  }
}
