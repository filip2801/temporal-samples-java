package io.temporal.samples.springboot.auctions;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Auction {

  @Id private UUID id;
  private Long carId;
  private LocalDateTime startTime;
  private LocalDateTime expectedEndTime;

  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  private Long highestBid = 0L;
  private String leadingBidder;

  // getters and setters
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getCarId() {
    return carId;
  }

  public void setCarId(Long carId) {
    this.carId = carId;
  }

  public LocalDateTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }

  public LocalDateTime getExpectedEndTime() {
    return expectedEndTime;
  }

  public void setExpectedEndTime(LocalDateTime expectedEndTime) {
    this.expectedEndTime = expectedEndTime;
  }

  public AuctionStatus getStatus() {
    return status;
  }

  public void setStatus(AuctionStatus status) {
    this.status = status;
  }

  public Long getHighestBid() {
    return highestBid;
  }

  public void setHighestBid(Long highestBid) {
    this.highestBid = highestBid;
  }

  public String getLeadingBidder() {
    return leadingBidder;
  }

  public void setLeadingBidder(String leadingBidder) {
    this.leadingBidder = leadingBidder;
  }
}
