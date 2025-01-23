package io.workshop.orquestation;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import io.workshop.orquestation.model.CreditCardPayment;
import io.workshop.orquestation.model.Customer;
import io.workshop.orquestation.payCreditCard.CreditCardPaymentWorkflow;

public class Starter {
    public static void main(String[] args) throws Exception {

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        WorkflowClient client = WorkflowClient.newInstance(service);

        CreditCardPayment order = createPaymentOrder();

        String workflowID = String.format("credit-card-payment-workflow-%s", order.getOrderNumber());

        WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
                .setWorkflowId(workflowID)
                .setTaskQueue(Constants.TASK_QUEUE_NAME)
                .build();

        CreditCardPaymentWorkflow workflow = client.newWorkflowStub(CreditCardPaymentWorkflow.class, workflowOptions);

        String orderConfirmation = workflow.payCreditCard(order);

        System.out.printf("Workflow result: %s\n", orderConfirmation);
        System.exit(0);
    }

    private static CreditCardPayment createPaymentOrder() {
        Customer customer = new Customer(8675309, "Israel Flores", "example@example.com", "555-555-0000");

        CreditCardPayment order = new CreditCardPayment("XD001", customer, 50000);

        return order;
    }
}
