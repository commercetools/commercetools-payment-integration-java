package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.Payment;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultImpl implements PaymentCreationResult {

    private OperationResult operationResult;
    @Nullable
    private Payment payment;
    private HandlingTask handlingTask;

    public PaymentCreationResultImpl(OperationResult operationResult, Payment payment, HandlingTask handlingTask) {
        this.operationResult = operationResult;
        this.payment = payment;
        this.handlingTask = handlingTask;
    }

    @Override
    public OperationResult getOperationResult() {

        return this.operationResult;
    }

    @Override
    public Optional<Payment> getCreatedPaymentObject() {

        return Optional.ofNullable(this.payment);
    }

    @Override
    public HandlingTask getHandlingTask() {

        return this.handlingTask;
    }
}
