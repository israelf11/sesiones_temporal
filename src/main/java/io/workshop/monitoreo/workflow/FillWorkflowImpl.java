package io.workshop.monitoreo.workflow;

import io.temporal.workflow.Workflow;

import java.time.Duration;

public class FillWorkflowImpl implements FillWorkflow {
    @Override
    public String exec(String input) {
        for(int i = 0; i < 50; i++) {
            Workflow.sleep(Duration.ofMillis(500));
        }
        return "done";
    }
}
