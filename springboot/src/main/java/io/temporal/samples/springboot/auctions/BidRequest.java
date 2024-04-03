package io.temporal.samples.springboot.auctions;

public class BidRequest {
  String bidder;
  Long bidValue;

  public String getBidder() {
    return bidder;
  }

  public void setBidder(String bidder) {
    this.bidder = bidder;
  }

  public Long getBidValue() {
    return bidValue;
  }

  public void setBidValue(Long bidValue) {
    this.bidValue = bidValue;
  }
}
