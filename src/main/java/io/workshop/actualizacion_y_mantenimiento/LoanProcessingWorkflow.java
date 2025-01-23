package io.workshop.actualizacion_y_mantenimiento;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import io.workshop.actualizacion_y_mantenimiento.model.CustomerInfo;

@WorkflowInterface
public interface LoanProcessingWorkflow {

  @WorkflowMethod
  public String loanProcessingWorkflow(CustomerInfo info);

}
