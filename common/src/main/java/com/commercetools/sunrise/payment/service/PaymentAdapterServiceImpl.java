package com.commercetools.sunrise.payment.service;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataProvider;
import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/18/16.
 */
public class PaymentAdapterServiceImpl implements PaymentAdapterService {

    @Override
    public List<PaymentServiceProvider> findAllPaymentServiceProviders() {
        return null;
    }

    @Override
    public CompletionStage<List<PaymentMethodInfo>> findAvailablePaymentMethods() {
        return null;
    }

    @Override
    public void createPayment(String methodID, CreatePaymentDataProvider data) {

    }

    @Override
    public PaymentStatus getPaymentStatus(String ref) {
        return null;
    }
}
