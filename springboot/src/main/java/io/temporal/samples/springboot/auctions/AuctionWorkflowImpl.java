package io.temporal.samples.springboot.auctions;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WorkflowImpl(taskQueues = "AuctionTaskQueue")
public class AuctionWorkflowImpl implements AuctionWorkflow {

  private boolean makingBidInProgress = false;

  private static final Logger logger = LoggerFactory.getLogger(AuctionWorkflowImpl.class);

  private final ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build();

  private final AuctionService auctionService =
      Workflow.newActivityStub(AuctionService.class, options);

  @Override
  public void startAuction(UUID auctionId, Long carId) {
    var auction = auctionService.startAuction(auctionId, carId);

    while (true) {
      if (auction.getStatus() == AuctionStatus.RUNNING) {
        logger.info("Calling Workflow.sleep for auction {}", auction.getId());
        Workflow.sleep(Duration.between(LocalDateTime.now(), auction.getExpectedEndTime()));
        auction = auctionService.endAuction(auctionId);
      } else {
        logger.info("Auction {} ended", auction.getId());
        return;
      }
    }
    // todo notifications
  }

  @Override
  public void makeBid(UUID auctionId, String bidder, Long bidValue) {
    Workflow.await(() -> !makingBidInProgress);

    makingBidInProgress = true;
    BidStatus bidStatus = auctionService.makeBid(auctionId, bidder, bidValue);
    makingBidInProgress = false;

    // todo save bid in db, how?
  }
}
