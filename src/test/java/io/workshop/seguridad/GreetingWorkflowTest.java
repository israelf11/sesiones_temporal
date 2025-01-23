package io.workshop.seguridad;

import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowRule;
import io.temporal.testing.WorkflowReplayer;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GreetingWorkflowTest {

    @Rule
    public TestWorkflowRule testWorkflowRule =
            TestWorkflowRule.newBuilder()
                    .setWorkflowTypes(GreetingWorkflowImpl.class)
                    .build();

    private final Customer testCustomer = new Customer("Elisabeth", "Ms.", "English Spansh", 22);

    @Test
    public void testWorkflow() throws Exception {
        GreetingWorkflow workflow =
                testWorkflowRule
                        .getWorkflowClient()
                        .newWorkflowStub(
                                GreetingWorkflow.class,
                                WorkflowOptions.newBuilder().setTaskQueue(testWorkflowRule.getTaskQueue()).build());

        String greeting = workflow.greet(testCustomer);
        assertEquals("Hello " + testCustomer.getName(), greeting);

        testWorkflowRule.getTestEnvironment().shutdown();
    }

//    @Test
//    public void replayFromHistory() throws Exception {
//        WorkflowReplayer.replayWorkflowExecutionFromResource(
//                "history-contentSize.json", GreetingWorkflowImpl.class);
//    }
}