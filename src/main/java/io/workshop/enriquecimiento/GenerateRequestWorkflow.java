package io.workshop.enriquecimiento;

import io.workshop.enriquecimiento.models.RequestToService;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface GenerateRequestWorkflow {
    @WorkflowMethod
    public RequestToService generateRequest(String id);
}