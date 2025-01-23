package io.workshop.enriquecimiento;

import io.workshop.enriquecimiento.models.Balance;
import io.workshop.enriquecimiento.models.BankAccount;
import io.workshop.enriquecimiento.models.User;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface GenerateRequestActivities {
  public Balance balanceActivity(String id);

  public BankAccount bankAccountActivity(String id);

  public User userActivity(String id);
}
