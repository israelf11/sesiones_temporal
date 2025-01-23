package io.workshop.monitoreo.activities;

import io.temporal.activity.Activity;
import io.temporal.activity.ActivityExecutionContext;

public class MetricsActivitiesImpl implements MetricsActivities {

  @Override
  public String performA(String input) {
    if (Activity.getExecutionContext().getInfo().getAttempt() < 3) {
      incRetriesCustomMetric(Activity.getExecutionContext());
      throw Activity.wrap(new NullPointerException("simulated"));
    }
    return "Performed activity A with input " + input + "\n";
  }

  @Override
  public String performB(String input) {
    if (Activity.getExecutionContext().getInfo().getAttempt() < 5) {
      incRetriesCustomMetric(Activity.getExecutionContext());
      throw Activity.wrap(new NullPointerException("simulated"));
    }
    return "Performed activity B with input " + input + "\n";
  }

  private void incRetriesCustomMetric(ActivityExecutionContext context) {
    context.getMetricsScope().counter("activity_retries").inc(1);
  }
}
