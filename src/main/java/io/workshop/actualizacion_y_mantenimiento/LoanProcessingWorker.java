package io.workshop.actualizacion_y_mantenimiento;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class LoanProcessingWorker {
  public static void main(String[] args) {

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkerFactory factory = WorkerFactory.newInstance(client);

    Worker worker = factory.newWorker(Constants.taskQueueName);

    worker.registerWorkflowImplementationTypes(LoanProcessingWorkflowImpl.class);

    worker.registerActivitiesImplementations(new LoanProcessingActivitiesImpl());

    factory.start();
  }
}
