package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class PayonePayment implements Payment {

    @Override
    public CompletionStage<List<PaymentMethodInfo>> getAvailablePaymentMethods() {
        return null;
    }

    @Override
    public void createPayment() {

    }

    @Override
    public void getPaymentStatus() {

    }
}
