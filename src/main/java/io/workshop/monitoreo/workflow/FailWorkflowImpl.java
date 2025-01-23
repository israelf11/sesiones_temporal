package io.workshop.monitoreo.workflow;

import io.temporal.workflow.Workflow;

import java.time.Duration;

public class FailWorkflowImpl implements FailWorkflow {
    @Override
    public String exec(String input) {
        for(int i = 0; i < 20; i++) {
            Workflow.sleep(Duration.ofMillis(500));
        }
        throw new IllegalArgumentException("simulated error...");
    }
}
