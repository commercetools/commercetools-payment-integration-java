package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.CreatePaymentTransactionData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionType;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mgatz on 7/27/16.
 */
public class CreatePaymentTransactionDataImpl extends PaymentInteractionDataBase implements CreatePaymentTransactionData {

    private final String paymentRef;
    private Payment payment;

    @Nullable
    private TransactionType transactionType;

    public CreatePaymentTransactionDataImpl(final SphereClient client, final String paymentRef, final Map<String, String> config) {
        super(client, config);
        this.paymentRef = paymentRef;
    }

    @Override
    public String getPaymentRef() {

        return this.paymentRef;
    }

    @Override
    public Payment getPayment() {
        return this.payment;
    }

    @Override
    public void setPayment(Payment p) {

        this.payment = p;
    }

    @Override
    public Optional<TransactionType> getTransactionType() {
        return Optional.ofNullable(this.transactionType);
    }

    @Override
    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
    }

}
