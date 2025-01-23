package io.workshop.enriquecimiento;

import io.workshop.enriquecimiento.models.Balance;
import io.workshop.enriquecimiento.models.BankAccount;
import io.workshop.enriquecimiento.models.RequestToService;
import io.workshop.enriquecimiento.models.User;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class GenerateRequestWorkflowImpl implements GenerateRequestWorkflow {
    ActivityOptions options = ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10)) // Tiempo máximo por intento
            .setRetryOptions(RetryOptions.newBuilder()
                    .setInitialInterval(Duration.ofSeconds(2)) // Tiempo entre reintentos
                    .setBackoffCoefficient(2.0) // Exponencial
                    .setMaximumAttempts(5) // Máximo de intentos
                    .build())
            .build();

    private final GenerateRequestActivities activities =
            Workflow.newActivityStub(GenerateRequestActivities.class, options);

    @Override
    public RequestToService generateRequest(String id) {
        Promise<Balance> balanceActivityPromise = Async.function(activities::balanceActivity, id);
        Promise<BankAccount> bankAccountActivityPromise = Async.function(activities::bankAccountActivity, id);
        Promise<User> userActivityPromise = Async.function(activities::userActivity, id);

        RequestToService request = new RequestToService();
        request.setBalance(balanceActivityPromise.get());
        request.setBankAccount(bankAccountActivityPromise.get());
        request.setUser(userActivityPromise.get());

        return request;
    }

}