package io.temporal.samples.springboot.bankAccount;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MoneyTransferWorkflow {

  @WorkflowMethod
  void transfer(String from, String to, Long amount);
}
