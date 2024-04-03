package io.temporal.samples.springboot.bankAccount;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

  private final BankAccountRepository bankAccountRepository;

  private final WorkflowClient client;

  public BankAccountController(BankAccountRepository bankAccountRepository, WorkflowClient client) {
    this.bankAccountRepository = bankAccountRepository;
    this.client = client;
  }

  @GetMapping
  List<BankAccount> getAllBankAccounts() {
    return bankAccountRepository.findAll();
  }

  @PostMapping("/transfers")
  void makeTransaction(@RequestBody MoneyTransfer transfer) {

    MoneyTransferWorkflow workflow =
        client.newWorkflowStub(
            MoneyTransferWorkflow.class,
            WorkflowOptions.newBuilder()
                .setTaskQueue("MoneyTransferTaskQueue")
                .setWorkflowId("MoneyTransfer-" + UUID.randomUUID())
                .build());

    WorkflowClient.start(() -> workflow.transfer(transfer.from, transfer.to, transfer.amount));
  }
}
