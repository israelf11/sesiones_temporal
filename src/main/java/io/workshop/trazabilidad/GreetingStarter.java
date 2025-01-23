package io.workshop.trazabilidad;

import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.workshop.trazabilidad.S1WFUtils.client;
import static io.workshop.trazabilidad.S1WFUtils.taskQueue;

public class GreetingStarter {
    // Dummy operations
    private static final Operation operation1 = new Operation("Elisabeth", "Cargo", 20.50);
    private static final Operation operation2 = new Operation("Michael", "Cargo", 32.99);
    private static final Operation operation3 = new Operation("John", "Cargo", 19.25);
    private static final Operation operation4 = new Operation("Ivan", "Cargo", 44.62);
    private static final Operation operation5 = new Operation("Donna", "Cargo", 50.53);
    private static final Operation operation6 = new Operation("Michael", "Abono", 32.99);
    private static final Operation operation7 = new Operation("John", "Abono", 19.25);
    private static final Operation operation8 = new Operation("Ivan", "Abono", 44.62);
    private static final Operation operation9 = new Operation("Donna", "Abono", 50.53);
    private static final List<Operation> allOperations = Arrays.asList(operation1, operation2, operation3, operation4, operation5, operation6, operation7, operation8, operation9);

    private static final String workflowId = "test-1";

    public static void main(String[] args) {
        for (Operation operation : allOperations) {
            Map<String, Object> searchAttributes = new HashMap<>();
            searchAttributes.put("OperationName", operation.getName());
            searchAttributes.put("OperationType", operation.getOperation());
            searchAttributes.put("OperationAmount", operation.getAmount());

            WorkflowOptions newCustomerWorkflowOptions = WorkflowOptions.newBuilder().setTaskQueue(taskQueue)
                    .setSearchAttributes(searchAttributes).build();

            GreetingWorkflow workflow = client.newWorkflowStub(GreetingWorkflow.class, newCustomerWorkflowOptions);

            WorkflowClient.start(workflow::greet, operation);
        }
    }
}
