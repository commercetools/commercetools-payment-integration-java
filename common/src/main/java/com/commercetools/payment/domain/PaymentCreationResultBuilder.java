package com.commercetools.payment.domain;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.impl.PaymentCreationResultImpl;
import io.sphere.sdk.payments.Payment;

/**
 * Builder to create a {@link PaymentCreationResult} object.
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultBuilder {
    private OperationResult operationResult;
    private Payment payment;
    private HandlingTask handlingTask;
    private Throwable exception;
    private String message;

    private PaymentCreationResultBuilder(OperationResult operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * Create the builder instance.
     * @param operationResult
     * @return new instance of the builder object
     */
    public static PaymentCreationResultBuilder of(final OperationResult operationResult) {
        return new PaymentCreationResultBuilder(operationResult);
    }

    public static PaymentCreationResult ofError(final String message) {
        return ofError(message, null);
    }

    public static PaymentCreationResult ofError(final String message,final Throwable exception) {
        return new PaymentCreationResultImpl(
                OperationResult.FAILED,
                null,
                HandlingTask.of(ShopAction.HANDLE_ERROR),
                message,
                exception);
    }

    /**
     * Add a payment object to the result.
     * @param payment the CTP {@link Payment} object
     * @return enriched self
     */
    public PaymentCreationResultBuilder payment(final Payment payment) {
        this.payment = payment;
        return this;
    }

    /**
     * Add information of the {@link HandlingTask} the shop will recieve.
     * @param task the handling task describing the next action to be taken
     * @return enriched self
     */
    public PaymentCreationResultBuilder handlingTask(final HandlingTask task) {
        this.handlingTask = task;
        return this;
    }

    public PaymentCreationResultBuilder exception(final Throwable exception) {
        this.exception = exception;
        return this;
    }

    public PaymentCreationResultBuilder message(final String message) {
        this.message = message;
        return this;
    }

    /**
     * @return the created {@link PaymentCreationResult} object.
     */
    public PaymentCreationResult build() {
        return new PaymentCreationResultImpl(
                this.operationResult,
                this.payment,
                this.handlingTask,
                this.message,
                this.exception);
    }
}
