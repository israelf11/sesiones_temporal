package io.workshop.monitoreo.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MetricsWorkflow {
  @WorkflowMethod
  String exec(String input);
}
