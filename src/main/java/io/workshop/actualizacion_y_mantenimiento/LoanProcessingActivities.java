package io.workshop.actualizacion_y_mantenimiento;

import io.temporal.activity.ActivityInterface;
import io.workshop.actualizacion_y_mantenimiento.model.ChargeInput;
import io.workshop.actualizacion_y_mantenimiento.model.CustomerInfo;

@ActivityInterface
public interface LoanProcessingActivities {

  public String chargeCustomer(ChargeInput input);

  public String sendThankYouToCustomer(CustomerInfo input);

}
