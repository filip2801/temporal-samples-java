package io.temporal.samples.springboot.bankAccount;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface BankAccountService {

  void withdraw(String from, Long amount);

  void deposit(String to, Long amount);
}
