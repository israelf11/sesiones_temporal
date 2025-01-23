package io.workshop.seguridad;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkflowImplementationOptions;

import static io.workshop.seguridad.S1WFUtils.clientTLS;
import static io.workshop.seguridad.S1WFUtils.taskQueue;

public class GreetingWorkerTLS {
    public static void main(String[] args) {
        WorkerFactory workerFactory = WorkerFactory.newInstance(clientTLS);
        Worker worker = workerFactory.newWorker(taskQueue);


        WorkflowImplementationOptions workflowImplementationOptions =
                WorkflowImplementationOptions.newBuilder()
                        .setFailWorkflowExceptionTypes(NullPointerException.class)
                        .build();

        worker.registerWorkflowImplementationTypes(workflowImplementationOptions, GreetingWorkflowImpl.class);

        workerFactory.start();
    }
}
