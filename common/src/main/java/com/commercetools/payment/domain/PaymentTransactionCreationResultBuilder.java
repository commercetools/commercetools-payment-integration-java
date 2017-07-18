package com.commercetools.payment.domain;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.model.impl.PaymentTransactionCreationResultImpl;
import io.sphere.sdk.payments.Payment;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentTransactionCreationResultBuilder {
    private OperationResult operationResult;
    private Payment payment;
    private HandlingTask handlingTask;
    private Throwable exception;
    private String message;

    private PaymentTransactionCreationResultBuilder(OperationResult operationResult) {
        this.operationResult = operationResult;
    }

    /**
     * Create the builder instance.
     *
     * @param operationResult operation result to parse in {@link PaymentTransactionCreationResultBuilder}
     * @return new instance of the builder object
     */
    public static PaymentTransactionCreationResultBuilder of(OperationResult operationResult) {
        return new PaymentTransactionCreationResultBuilder(operationResult);
    }

    /**
     * Default success action - {@link ShopAction#CONTINUE}
     * @param payment payment reference to set in the result builder.
     * @return result builder with {@link OperationResult#SUCCESS} and {@link ShopAction#CONTINUE}
     */
    public static PaymentTransactionCreationResultBuilder ofSuccess(Payment payment) {
        return of(OperationResult.SUCCESS)
                .payment(payment)
                .handlingTask(HandlingTask.of(ShopAction.CONTINUE));
    }

    public static PaymentTransactionCreationResult ofError(String message) {
        return ofError(message, null, null);
    }

    public static PaymentTransactionCreationResult ofError(String message, Throwable exception) {
        return ofError(message, exception, null);
    }

    public static PaymentTransactionCreationResult ofError(String message, Throwable exception, Payment payment) {
        return new PaymentTransactionCreationResultImpl(
                OperationResult.FAILED,
                payment,
                HandlingTask.of(ShopAction.HANDLE_ERROR),
                message,
                exception);
    }

    /**
     * Add a payment object to the result.
     *
     * @param payment the CTP {@link Payment} object
     * @return enriched self
     */
    public PaymentTransactionCreationResultBuilder payment(Payment payment) {
        this.payment = payment;
        return this;
    }

    /**
     * Add information of the {@link HandlingTask} the shop will recieve.
     *
     * @param task the handling task describing the next action to be taken
     * @return enriched self
     */
    public PaymentTransactionCreationResultBuilder handlingTask(HandlingTask task) {
        this.handlingTask = task;
        return this;
    }

    public PaymentTransactionCreationResultBuilder exception(Throwable exception) {
        this.exception = exception;
        return this;
    }

    /**
     * @return the created {@link PaymentCreationResult} object.
     */
    public PaymentTransactionCreationResult build() {
        return new PaymentTransactionCreationResultImpl(
                this.operationResult,
                this.payment,
                this.handlingTask,
                this.message,
                this.exception);
    }
}
