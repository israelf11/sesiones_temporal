package io.workshop.trazabilidad;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class GreetingWorkflowImpl implements GreetingWorkflow {

    private Operation operation;
    private final Logger logger = Workflow.getLogger(this.getClass().getName());

    @Override
    public String greet(Operation operation) {

        logger.info("My Id: " + Workflow.getInfo().getWorkflowId());
        logger.info("My runId" + Workflow.getInfo().getRunId());
        logger.info("My task queue: " + Workflow.getInfo().getTaskQueue());
        logger.info("Current time: " + Workflow.currentTimeMillis() + '\n');

        if (operation != null) {
            this.operation = operation;
        }

        return "Operation completed " + this.operation.getName();

    }
}
