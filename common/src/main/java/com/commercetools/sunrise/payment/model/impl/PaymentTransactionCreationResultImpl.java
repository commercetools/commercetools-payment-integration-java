package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentTransactionCreationResultImpl extends AbstractPaymentOperationResult implements PaymentTransactionCreationResult {

    private HandlingTask handlingTask;

    public PaymentTransactionCreationResultImpl(
            OperationResult operationResult,
            Payment payment,
            HandlingTask handlingTask) {
        super(operationResult, payment);
        this.handlingTask = handlingTask;
    }

    public PaymentTransactionCreationResultImpl(
            OperationResult operationResult,
            Payment payment,
            HandlingTask handlingTask,
            String message,
            Throwable exception) {
        super(operationResult, payment, message, exception);
        this.handlingTask = handlingTask;
    }

    @Override
    public HandlingTask getHandlingTask() {

        return this.handlingTask;
    }
}
