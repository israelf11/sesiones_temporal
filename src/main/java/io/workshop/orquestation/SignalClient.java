package io.workshop.orquestation;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.workshop.orquestation.payCreditCard.CreditCardPaymentWorkflow;

public class SignalClient {
    public static void main(String[] args) throws Exception {

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        WorkflowClient client = WorkflowClient.newInstance(service);

        CreditCardPaymentWorkflow workflow = client.newWorkflowStub(CreditCardPaymentWorkflow.class, "credit-card-payment-workflow-XD001");

        workflow.fulfillOrderSignal(true);

        System.exit(0);
    }
}