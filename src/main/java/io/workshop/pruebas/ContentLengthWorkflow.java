package io.workshop.pruebas;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ContentLengthWorkflow {
    @WorkflowMethod
    ContentLengthInfo execute();
}
