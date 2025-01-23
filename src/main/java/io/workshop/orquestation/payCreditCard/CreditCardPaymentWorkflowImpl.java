package io.workshop.orquestation.payCreditCard;

import io.temporal.workflow.Workflow;
import io.workshop.orquestation.model.Customer;
import io.workshop.orquestation.model.CreditCardPayment;

import org.slf4j.Logger;

import java.time.Duration;

public class CreditCardPaymentWorkflowImpl implements CreditCardPaymentWorkflow {

    public static final Logger logger = Workflow.getLogger(CreditCardPaymentWorkflowImpl.class);

    private boolean authorization;

    @Override
    public String payCreditCard(CreditCardPayment order) {

        String orderNumber = order.getOrderNumber();
        Customer customer = order.getCustomer();
        double amount = order.getAmount();


        logger.info("payment Workflow Invoked");

        Workflow.await(Duration.ofSeconds(25), () -> this.authorization);

        String confirmation;

        if (this.authorization) {
            logger.info("Payment was fulfilled");
            confirmation = "Success";
        } else {
            logger.info("Payment was not fulfilled");
            confirmation = "Not paid";
        }

        return confirmation;

    }

    @Override
    public void fulfillOrderSignal(boolean bool) {
        this.authorization = bool;
    }
}
