package io.workshop.enriquecimiento;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class GenerateRequestWorker {
  public static void main(String[] args) {
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkerFactory factory = WorkerFactory.newInstance(client);
    Worker worker = factory.newWorker("queue-tasks");
    worker.registerWorkflowImplementationTypes(GenerateRequestWorkflowImpl.class);
    worker.registerActivitiesImplementations(new GenerateRequestActivitiesImpl());
    factory.start();
  }
}