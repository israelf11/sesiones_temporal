package io.workshop.trazabilidad;

import com.google.protobuf.ByteString;
import io.temporal.api.workflow.v1.WorkflowExecutionInfo;
import io.temporal.api.workflowservice.v1.ListWorkflowExecutionsRequest;
import io.temporal.api.workflowservice.v1.ListWorkflowExecutionsResponse;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.Optional;


public class S1WFUtils {
    static WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    public static final WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
            .build());

    public static final String taskQueue = "greeting";

    public static final RetryOptions NO_RETRY = RetryOptions.newBuilder().setMaximumAttempts(1).build();

    private static void listWorkflowExecutionsWithQuery(String query, ByteString token) {

        ListWorkflowExecutionsRequest request;

        if (token == null) {
            request =
                    ListWorkflowExecutionsRequest.newBuilder()
                            .setNamespace(client.getOptions().getNamespace())
                            .setQuery(query)
                            .build();
        } else {
            request =
                    ListWorkflowExecutionsRequest.newBuilder()
                            .setNamespace(client.getOptions().getNamespace())
                            .setQuery(query)
                            .setNextPageToken(token)
                            .build();
        }
        ListWorkflowExecutionsResponse response =
                service.blockingStub().listWorkflowExecutions(request);
        for (WorkflowExecutionInfo workflowExecutionInfo : response.getExecutionsList()) {
            System.out.println("Workflow ID: " + workflowExecutionInfo.getExecution().getWorkflowId() + " Run ID: " +
                    workflowExecutionInfo.getExecution().getRunId() + " Status: " + workflowExecutionInfo.getStatus());
            if (workflowExecutionInfo.getParentExecution() != null) {
                System.out.println("****** PARENT: " + workflowExecutionInfo.getParentExecution().getWorkflowId() + " - " +
                        workflowExecutionInfo.getParentExecution().getRunId());
            }
        }

        if (response.getNextPageToken() != null && response.getNextPageToken().size() > 0) {
            listWorkflowExecutionsWithQuery(query, response.getNextPageToken());
        }
    }

    private static void printWorkflowExecutionsAndShowOperationInfo(String query) {
        ListWorkflowExecutionsRequest listWorkflowExecutionRequest =
                ListWorkflowExecutionsRequest.newBuilder()
                        .setNamespace(client.getOptions().getNamespace())
                        .setQuery(query)
                        .build();
        ListWorkflowExecutionsResponse listWorkflowExecutionsResponse =
                service.blockingStub().listWorkflowExecutions(listWorkflowExecutionRequest);

        for (WorkflowExecutionInfo workflowExecutionInfo : listWorkflowExecutionsResponse.getExecutionsList()) {

            WorkflowStub existingUntyped = client.newUntypedWorkflowStub(workflowExecutionInfo.getExecution().getWorkflowId(),
                    Optional.empty(), Optional.empty());
            Operation operation = existingUntyped.query("getOperation", Operation.class);
            System.out.println("Workflow ID: " + workflowExecutionInfo.getExecution().getWorkflowId()
                    + " Status: " + workflowExecutionInfo.getStatus()
                    + " Operation: " + operation);
        }
    }

    public static void main(String[] args) {
//        // 2) for searchAttributes
//        // Using built-in search attributes
//        listWorkflowExecutionsWithQuery("ExecutionStatus='Completed'", null);
//        // Operation workflows where operation is over 20
//        printWorkflowExecutionsAndShowOperationInfo("OperationAge > 20");
//        // Operation workflows where operation title is Ms and age is over 30
//        printWorkflowExecutionsAndShowOperationInfo("OperationAge > 20 AND OperationTitle='Ms'");
//        // Operation workflows where operation speaks Spanish
//        printWorkflowExecutionsAndShowOperationInfo("OperationLanguages LIKE \"%Spanish\"");
        printWorkflowExecutionsAndShowOperationInfo("OperationLanguages STARTS_WITH \"Spanish\"");

    }
}
