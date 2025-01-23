package io.workshop.monitoreo.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.workshop.monitoreo.activities.MetricsActivities;

import java.time.Duration;

public class MetricsWorkflowImpl implements MetricsWorkflow {

  private final MetricsActivities activities =
      Workflow.newActivityStub(
          MetricsActivities.class,
          ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

  @Override
  public String exec(String input) {
    String result = activities.performA(input);
    Workflow.sleep(Duration.ofSeconds(5));
    result += activities.performB(input);

    return result;
  }
}
