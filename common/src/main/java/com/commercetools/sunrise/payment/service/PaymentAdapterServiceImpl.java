package com.commercetools.sunrise.payment.service;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CompletionStage;

/**
 * Provides access to the payment service provider handling methods.
 *
 * Created by mgatz on 7/18/16.
 */
public class PaymentAdapterServiceImpl implements PaymentAdapterService {

    private ServiceLoader<PaymentServiceProvider> pspLoader;

    public PaymentAdapterServiceImpl() {

        pspLoader = ServiceLoader.load(PaymentServiceProvider.class);
    }

    @Override
    public List<PaymentServiceProvider> findAllPaymentServiceProviders() {
        List<PaymentServiceProvider> result = new ArrayList<>();

        pspLoader.forEach(psp -> {
            result.add(psp);
        });

        return result;
    }

    @Override
    public List<PaymentMethodInfo> findAvailablePaymentMethods() {
        return null;
    }

    @Override
    public CompletionStage<PaymentCreationResult> createPayment(String interfaceId, String methodID, CreatePaymentData data) {
        // lookup implementation needed to be called
        PaymentServiceProvider provider = findAllPaymentServiceProviders().stream().filter(psp -> psp.getId().equals(interfaceId)).findFirst().get();

        // let the method be created and apply the passed data
        return provider.provideCreatePaymentHandler(methodID).apply(data);
    }

    @Override
    public void createPaymentTransaction(String paymentRef) {

    }

    @Override
    public void createPaymentTransaction(String paymentRef, Map<String, String> configData) {

    }

    @Override
    public void createPaymentTransaction(CreatePaymentData data) {

    }

    @Override
    public void createPaymentTransaction(CreatePaymentData data, Map<String, String> configData) {

    }

    @Override
    public PaymentStatus getPaymentStatus(String ref) {
        return null;
    }
}
