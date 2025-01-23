package io.workshop.trazabilidad;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkflowImplementationOptions;

import static io.workshop.trazabilidad.S1WFUtils.taskQueue;
import static io.workshop.trazabilidad.S1WFUtils.client;

public class GreetingWorker {
    public static void main(String[] args) {
        WorkflowImplementationOptions workflowImplementationOptions =
                WorkflowImplementationOptions.newBuilder()
                        .setFailWorkflowExceptionTypes(NullPointerException.class)
                        .build();

        WorkerFactory workerFactory = WorkerFactory.newInstance(client);
        Worker worker = workerFactory.newWorker(taskQueue);

        worker.registerWorkflowImplementationTypes(workflowImplementationOptions, GreetingWorkflowImpl.class);

        workerFactory.start();
    }
}
