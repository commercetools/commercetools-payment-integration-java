package com.commercetools.payment.model.impl;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.Payment;

/**
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultImpl extends AbstractPaymentOperationResult implements PaymentCreationResult {

    private final HandlingTask handlingTask;

    public PaymentCreationResultImpl(OperationResult operationResult, Payment payment, HandlingTask handlingTask) {
        super(operationResult, payment);
        this.handlingTask = handlingTask;
    }

    public PaymentCreationResultImpl(
            final OperationResult operationResult,
            final Payment payment,
            final HandlingTask handlingTask,
            final String message,
            final Throwable exception) {
        super(operationResult, payment, message, exception);
        this.handlingTask = handlingTask;
    }



    @Override
    public HandlingTask getHandlingTask() {

        return this.handlingTask;
    }
}
