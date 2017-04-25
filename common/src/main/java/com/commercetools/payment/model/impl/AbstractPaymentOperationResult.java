package com.commercetools.payment.model.impl;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.model.PaymentOperationResult;
import io.sphere.sdk.payments.Payment;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 8/8/16.
 */
public abstract class AbstractPaymentOperationResult implements PaymentOperationResult {
    private OperationResult operationResult;
    @Nullable
    private Payment payment;
    @Nullable
    private Throwable exception;
    @Nullable
    private String message;

    protected AbstractPaymentOperationResult(OperationResult operationResult, Payment payment) {
        this(operationResult, payment, null, null);
    }

    protected AbstractPaymentOperationResult(OperationResult operationResult, Payment payment, String message, Throwable exception) {
        this.operationResult = operationResult;
        this.payment = payment;
        this.message = message;
        this.exception = exception;
    }


    @Override
    public OperationResult getOperationResult() {

        return this.operationResult;
    }

    @Override
    public Optional<Payment> getRelatedPaymentObject() {

        return Optional.ofNullable(this.payment);
    }

    @Override
    public Optional<Throwable> getException() {

        return Optional.ofNullable(this.exception);
    }

    @Override
    public Optional<String> getMessage() {

        return Optional.ofNullable(this.message);
    }
}
