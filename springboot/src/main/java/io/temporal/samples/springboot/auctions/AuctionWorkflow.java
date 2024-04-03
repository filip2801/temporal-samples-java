package io.temporal.samples.springboot.auctions;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.UUID;

@WorkflowInterface
public interface AuctionWorkflow {

  @WorkflowMethod
  void startAuction(UUID auctionId, Long carId);

  @SignalMethod
  void makeBid(UUID auctionId, String bidder, Long bidValue);
}
