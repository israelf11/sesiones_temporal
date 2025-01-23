package io.workshop.seguridad;

import io.temporal.client.WorkflowFailedException;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.TimeoutFailure;

import javax.net.ssl.SSLException;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.workshop.seguridad.S1WFUtils.clientTLS;
import static io.workshop.seguridad.S1WFUtils.taskQueue;

public class GreetingStarterTLS {
    private static final Customer customer1 = new Customer("Elisabeth", "Ms", "English Spanish", 20);

    private static final String workflowId = "test-1";

    public static void main(String[] args) throws ExecutionException, InterruptedException, SSLException, FileNotFoundException {

        startTypedAndWaitForResult();

        System.exit(0);
    }

    private static void startTypedAndWaitForResult() {
        GreetingWorkflow workflow = clientTLS.newWorkflowStub(
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
            if(e.getCause() instanceof TimeoutFailure) {
                System.out.println(e.getCause().getMessage());
            }
        }
    }

}
