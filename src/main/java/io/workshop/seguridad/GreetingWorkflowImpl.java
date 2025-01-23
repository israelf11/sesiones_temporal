package io.workshop.seguridad;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;

public class GreetingWorkflowImpl implements GreetingWorkflow {

    private Customer customer;
    private final Logger logger = Workflow.getLogger(this.getClass().getName());

    @Override
    public String greet(Customer customer) {

        logger.info("My Id: " + Workflow.getInfo().getWorkflowId());
        logger.info("My runId: " + Workflow.getInfo().getRunId());
        logger.info("My task queue: " + Workflow.getInfo().getTaskQueue());
        logger.info("Current time: " + Workflow.currentTimeMillis());

        if (customer != null) {
            this.customer = customer;
        }

//        Workflow.sleep(Duration.ofSeconds(100));

        return "Hello " + this.customer.getName();
    }
}
