package io.workshop.pruebas;

import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import static io.workshop.pruebas.S2WFUtils.client;
import static io.workshop.pruebas.S2WFUtils.taskQueue;

public class ContentWorker {
    public static void main(String[] args) {

        WorkerFactory workerFactory = WorkerFactory.newInstance(client);
        Worker worker = workerFactory.newWorker(taskQueue);

        worker.registerWorkflowImplementationTypes(ContentLengthWorkflowImpl.class);

        worker.registerActivitiesImplementations(new ContentLengthActivityImpl());

        workerFactory.start();
    }
}
