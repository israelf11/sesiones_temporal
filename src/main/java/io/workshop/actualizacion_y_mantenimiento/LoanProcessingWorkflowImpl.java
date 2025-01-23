package io.workshop.actualizacion_y_mantenimiento;

import org.slf4j.Logger;

import io.workshop.actualizacion_y_mantenimiento.model.CustomerInfo;
import io.workshop.actualizacion_y_mantenimiento.model.ChargeInput;

import io.temporal.common.SearchAttributeKey;
import io.temporal.workflow.Workflow;
import io.temporal.activity.ActivityOptions;

import java.util.Arrays;
import java.util.List;
import java.time.Duration;

public class LoanProcessingWorkflowImpl implements LoanProcessingWorkflow {

    public static final Logger logger = Workflow.getLogger(LoanProcessingWorkflowImpl.class);

    public static final SearchAttributeKey<List<String>> TEMPORAL_CHANGE_VERSION = SearchAttributeKey.forKeywordList("TemporalChangeVersion");

    ActivityOptions options =
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(5)).build();

    private final LoanProcessingActivities activities =
            Workflow.newActivityStub(LoanProcessingActivities.class, options);

    public String loanProcessingWorkflow(CustomerInfo info) {

        String customerId = info.getCustomerID();
        int amount = info.getAmount();
        int numberOfPeriods = info.getNumberOfPeriods();

        int totalPaid = 0;
        /* Comentar en primera ejecucion */
        String versionKey = "MovedThankYouAfterLoop";
        int version = Workflow.getVersion(versionKey, Workflow.DEFAULT_VERSION, 1);

        if (version != Workflow.DEFAULT_VERSION) {
            Workflow.upsertTypedSearchAttributes(TEMPORAL_CHANGE_VERSION
                    .valueSet(Arrays.asList((versionKey + "-" + version))));
        }

        if (version == Workflow.DEFAULT_VERSION) {
            /* Fin comentario */
            String confirmation = activities.sendThankYouToCustomer(info);
            /* Comentar en primera ejecucion */
        }
        /* Fin comentario */

        for (int period = 1; period <= numberOfPeriods; period++) {

            ChargeInput chargeInput = new ChargeInput(customerId, amount, period, numberOfPeriods);

            activities.chargeCustomer(chargeInput);

            totalPaid += chargeInput.getAmount();
            logger.info("Payment complete for period: {} Total Paid: {}", period, totalPaid);

            Workflow.sleep(Duration.ofSeconds(5));
        }

        /* Comentar en primera ejecucion */
        if (version == 1) {
            String confirmation = activities.sendThankYouToCustomer(info);
        }
        /* Fin comentario */

        return String.format("Loan for customer %s has been fully paid (total=%d)", customerId,
                totalPaid);
    }
}
