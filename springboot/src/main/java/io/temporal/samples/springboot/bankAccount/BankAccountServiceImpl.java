package io.temporal.samples.springboot.bankAccount;

import io.temporal.spring.boot.ActivityImpl;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = "MoneyTransferTaskQueue")
public class BankAccountServiceImpl implements BankAccountService {

  private final BankAccountRepository bankAccountRepository;

  public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
    this.bankAccountRepository = bankAccountRepository;
  }

  // todo impossible idempotency if it would be http call?

  @Override
  public void withdraw(String from, Long amount) {
    var bankAccount = bankAccountRepository.findById(from).get();
    bankAccount.subAmount(amount);
    bankAccountRepository.save(bankAccount);
  }

  @Override
  public void deposit(String to, Long amount) {
    var bankAccount = bankAccountRepository.findById(to).get();
    bankAccount.addAmount(amount);
    bankAccountRepository.save(bankAccount);
  }
}
