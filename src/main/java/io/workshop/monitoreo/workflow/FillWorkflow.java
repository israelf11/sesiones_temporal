package io.workshop.monitoreo.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface FillWorkflow {
    @WorkflowMethod
    String exec(String input);
}
