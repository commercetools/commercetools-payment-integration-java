package com.commercetools.sunrise.payment.service;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
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
    public CompletionStage<PaymentCreationResult> createPayment(CreatePaymentData data) {
        // lookup implementation needed to be called
        PaymentServiceProvider provider = findPaymentServiceProvider(data.getPaymentMethodinInfo().getPaymentInterface());

        // let the method be created and apply the passed data
        return provider.provideCreatePaymentHandler(data.getPaymentMethodinInfo().getMethod()).apply(data);
    }

    @Override
    public CompletionStage<PaymentTransactionCreationResult> createPaymentTransaction(CreatePaymentTransactionData createData) {
        return PaymentLookupHelper.of(createData.getSphereClient())
                .findPaymentFor(createData)
                .thenCompose(data -> findPaymentServiceProvider(data.getPayment().getPaymentMethodInfo().getPaymentInterface())
                        .provideCreatePaymentTransactionHandler(data.getPayment().getPaymentMethodInfo().getMethod()).apply(data));
    }

    @Override
    public PaymentStatus getPaymentStatus(String ref) {
        return null;
    }


    private PaymentServiceProvider findPaymentServiceProvider(String paymentInterfaceId) {
        return findAllPaymentServiceProviders().stream()
                    .filter(psp -> psp.getId().equals(paymentInterfaceId)).findFirst().get();
    }
}