package io.workshop.pruebas;

import io.temporal.client.WorkflowOptions;

import static io.workshop.pruebas.S2WFUtils.client;
import static io.workshop.pruebas.S2WFUtils.taskQueue;

public class ContentStarter {
    private static final String workflowId = "c2ContentLengthWorkflow";

    public static void main(String[] args) {
        ContentLengthWorkflow workflow = client.newWorkflowStub(
                ContentLengthWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        //.setWorkflowExecutionTimeout(Duration.ofSeconds(20))
                        //.setWorkflowRunTimeout(Duration.ofSeconds(20))
                        .setTaskQueue(taskQueue)
                        .build()
        );

        ContentLengthInfo info = workflow.execute();

        System.out.println(info.toString());

        System.exit(0);
    }
}
