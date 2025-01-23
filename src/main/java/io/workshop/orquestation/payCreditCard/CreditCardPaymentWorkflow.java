package io.workshop.orquestation.payCreditCard;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.temporal.workflow.SignalMethod;
import io.workshop.orquestation.model.CreditCardPayment;

@WorkflowInterface
public interface CreditCardPaymentWorkflow {

  @WorkflowMethod
  String payCreditCard(CreditCardPayment order);

  @SignalMethod
  void fulfillOrderSignal(boolean bool);

}
