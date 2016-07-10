package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.impl.PaymentCreationResultImpl;
import io.sphere.sdk.payments.Payment;

/**
 * Builder to create a {@link PaymentCreationResult} object.
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultBuilder {
    private OperationResult operationResult;
    private Payment payment;
    private HandlingTask handlingTask;

    private PaymentCreationResultBuilder(OperationResult operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * Create the builder instance.
     * @param operationResult
     * @return new instance of the builder object
     */
    public static PaymentCreationResultBuilder of(OperationResult operationResult) {
        return new PaymentCreationResultBuilder(operationResult);
    }

    /**
     * Add a payment object to the result.
     * @param payment the CTP {@link Payment} object
     * @return enriched self
     */
    public PaymentCreationResultBuilder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    /**
     * Add information of the {@link HandlingTask} the shop will recieve.
     * @param task the handling task describing the next action to be taken
     * @return enriched self
     */
    public PaymentCreationResultBuilder handlingTask(HandlingTask task) {
        this.handlingTask = task;
        return this;
    }

    /**
     * @return the created {@link PaymentCreationResult} object.
     */
    public PaymentCreationResult build() {
        return new PaymentCreationResultImpl(this.operationResult, this.payment, this.handlingTask);
    }
}
