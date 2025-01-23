package io.workshop.enriquecimiento;

import io.workshop.enriquecimiento.models.RequestToService;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.time.Duration;

public class Starter {

    public static void main(String[] args) {


        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
                .build());

        GenerateRequestWorkflow workflow = client.newWorkflowStub(
                GenerateRequestWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowRunTimeout(Duration.ofSeconds(30))
                        .setRetryOptions(RetryOptions.newBuilder()
                                .setMaximumAttempts(3)
                                .setInitialInterval(Duration.ofSeconds(5))
                                .setBackoffCoefficient(2)
                                .setMaximumInterval(Duration.ofMinutes(1))
                                .build())
                        .setTaskQueue("queue-tasks")
                        .build()
        );

        RequestToService request = workflow.generateRequest("123");
        request.log();
    }
}
