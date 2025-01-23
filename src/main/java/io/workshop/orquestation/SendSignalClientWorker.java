package io.workshop.orquestation;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import io.workshop.orquestation.payCreditCard.CreditCardPaymentWorkflowImpl;

public class SendSignalClientWorker {
    public static void main(String[] args) {

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker(Constants.TASK_QUEUE_NAME);

        worker.registerWorkflowImplementationTypes(CreditCardPaymentWorkflowImpl.class);

        factory.start();
    }
}