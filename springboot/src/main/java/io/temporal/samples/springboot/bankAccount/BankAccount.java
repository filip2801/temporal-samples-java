package io.temporal.samples.springboot.bankAccount;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BankAccount {

  @Id private String owner;
  private Long amount;

  public BankAccount() {}

  public BankAccount(String owner, Long amount) {
    this.owner = owner;
    this.amount = amount;
  }

  public void addAmount(Long amountToAdd) {
    this.amount += amountToAdd;
  }

  public void subAmount(Long amountToSub) {
    if (amountToSub > this.amount) {
      throw new IllegalStateException(
          "amountToSub " + amountToSub + " is higher than account amount " + this.amount);
    }
    this.amount -= amountToSub;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }
}
