package io.temporal.samples.springboot.bankAccount;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import java.time.Duration;

@WorkflowImpl(taskQueues = "MoneyTransferTaskQueue") // todo taskQueues ?
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {

  private final ActivityOptions options =
      ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build();

  private final BankAccountService bankAccountService =
      Workflow.newActivityStub(BankAccountService.class, options);

  @Override
  public void transfer(String from, String to, Long amount) {
    bankAccountService.withdraw(from, amount);
    bankAccountService.deposit(to, amount);
  }
}
