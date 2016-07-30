package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.model.impl.PaymentTransactionCreationResultImpl;
import io.sphere.sdk.payments.Payment;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentTransactionCreationResultBuilder {
    private OperationResult operationResult;
    private Payment payment;
    private HandlingTask handlingTask;

    private PaymentTransactionCreationResultBuilder(OperationResult operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * Create the builder instance.
     * @param operationResult
     * @return new instance of the builder object
     */
    public static PaymentTransactionCreationResultBuilder of(OperationResult operationResult) {
        return new PaymentTransactionCreationResultBuilder(operationResult);
    }

    public static PaymentTransactionCreationResult ofError() {
        return new PaymentTransactionCreationResultImpl(OperationResult.FAILED, null, HandlingTask.of(ShopAction.HANDLE_ERROR));
    }

    /**
     * Add a payment object to the result.
     * @param payment the CTP {@link Payment} object
     * @return enriched self
     */
    public PaymentTransactionCreationResultBuilder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    /**
     * Add information of the {@link HandlingTask} the shop will recieve.
     * @param task the handling task describing the next action to be taken
     * @return enriched self
     */
    public PaymentTransactionCreationResultBuilder handlingTask(HandlingTask task) {
        this.handlingTask = task;
        return this;
    }

    /**
     * @return the created {@link PaymentCreationResult} object.
     */
    public PaymentTransactionCreationResult build() {
        return new PaymentTransactionCreationResultImpl(this.operationResult, this.payment, this.handlingTask);
    }
}
