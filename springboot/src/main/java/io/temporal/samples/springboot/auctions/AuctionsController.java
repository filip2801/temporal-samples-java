package io.temporal.samples.springboot.auctions;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions")
public class AuctionsController {

  private static final Logger logger = LoggerFactory.getLogger(AuctionsController.class);

  private final AuctionRepository auctionRepository;

  private final WorkflowClient client;

  public AuctionsController(AuctionRepository auctionRepository, WorkflowClient client) {
    this.auctionRepository = auctionRepository;
    this.client = client;
  }

  @GetMapping
  List<Auction> getAllAuctions() {
    return auctionRepository.findAll();
  }

  @PostMapping
  UUID startAuction(@RequestBody StartAuctionRequest startAuctionRequest) {
    var auctionId = UUID.randomUUID();
    AuctionWorkflow workflow =
        client.newWorkflowStub(AuctionWorkflow.class, workflowOptions(auctionId));

    WorkflowClient.start(() -> workflow.startAuction(auctionId, startAuctionRequest.getCarId()));
    return auctionId;
  }

  private WorkflowOptions workflowOptions(UUID auctionId) {
    return WorkflowOptions.newBuilder()
        .setTaskQueue("AuctionTaskQueue")
        .setWorkflowId(getWorkflowId(auctionId))
        .build();
  }

  @PostMapping("/{auctionId}/bids")
  void makeBid(@PathVariable UUID auctionId, @RequestBody BidRequest bidRequest) {
    AuctionWorkflow workflow =
        client.newWorkflowStub(AuctionWorkflow.class, getWorkflowId(auctionId));
    workflow.makeBid(auctionId, bidRequest.getBidder(), bidRequest.getBidValue());
  }

  @PostMapping("/{auctionId}/multiple-bids")
  void makeBid(@PathVariable UUID auctionId, @RequestParam(defaultValue = "100") int numberOfBids) {

    int numberOfThreads = numberOfBids / 5;
    var executorService = Executors.newFixedThreadPool(numberOfThreads);

    for (int i = 0; i < numberOfBids; i++) {
      Long bidValue = 100L + i;
      executorService.submit(
          () -> {
            AuctionWorkflow workflow =
                client.newWorkflowStub(AuctionWorkflow.class, getWorkflowId(auctionId));
            workflow.makeBid(auctionId, UUID.randomUUID().toString(), bidValue);
          });
    }
  }

  private String getWorkflowId(UUID auctionId) {
    return "Auction-" + auctionId;
  }
}
