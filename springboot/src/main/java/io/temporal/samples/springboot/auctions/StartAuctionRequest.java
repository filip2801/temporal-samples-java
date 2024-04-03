package io.temporal.samples.springboot.auctions;

public class StartAuctionRequest {
  private Long carId;

  public Long getCarId() {
    return carId;
  }

  public void setCarId(Long carId) {
    this.carId = carId;
  }
}
