package io.workshop.seguridad;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowFailedException;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.TimeoutFailure;
import io.temporal.serviceclient.WorkflowServiceStubs;

import javax.net.ssl.SSLException;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

import static io.workshop.seguridad.S1WFUtils.taskQueue;

public class GreetingStarter {
    private static final Customer customer1 = new Customer("Elisabeth", "Ms", "English Spanish", 20);

    // Domain-specific workflow id
    private static final String workflowId = "test-2";

    public static void main(String[] args) throws ExecutionException, InterruptedException, SSLException, FileNotFoundException {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
                .build());

        GreetingWorkflow workflow = client.newWorkflowStub(
                GreetingWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setWorkflowRunTimeout(Duration.ofSeconds(30))
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setMaximumAttempts(3)
                                .setInitialInterval(Duration.ofSeconds(5))
                                .setBackoffCoefficient(2)
                                .setMaximumInterval(Duration.ofMinutes(1))
                                .build())
                        .setTaskQueue(taskQueue)
                        .build()
        );

        try {
            String greeting = workflow.greet(customer1);
            System.out.println("Greeting: " + greeting);
        } catch (WorkflowFailedException e) {
            if (e.getCause() instanceof TimeoutFailure) {
                System.out.println(e.getCause().getMessage());
            }
        }
    }
}
