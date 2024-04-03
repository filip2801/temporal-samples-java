package io.temporal.samples.springboot.bankAccount;

import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BankAccountsInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private final BankAccountRepository bankAccountRepository;

  public BankAccountsInitializer(BankAccountRepository bankAccountRepository) {
    this.bankAccountRepository = bankAccountRepository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    if (bankAccountRepository.findAll().isEmpty()) {
      bankAccountRepository.saveAll(
          List.of(
              new BankAccount("adam", 1000L),
              new BankAccount("ben", 1000L),
              new BankAccount("leo", 1000L),
              new BankAccount("zac", 1000L)));
    }
  }
}
