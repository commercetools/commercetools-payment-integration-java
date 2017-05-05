package com.commercetools.payment.service;

import com.commercetools.payment.domain.PaymentServiceProvider;
import com.commercetools.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Provides access to the payment service provider handling methods.
 *
 * Created by mgatz on 7/18/16.
 */
public class PaymentAdapterServiceImpl implements PaymentAdapterService {

    private final List<PaymentServiceProvider> paymentServiceProviders;
    private ServiceLoader<PaymentServiceProvider> pspLoader;

    public PaymentAdapterServiceImpl() {
        pspLoader = ServiceLoader.load(PaymentServiceProvider.class, PaymentAdapterServiceImpl.class.getClassLoader());
        paymentServiceProviders = new ArrayList<>();

        pspLoader.forEach(psp -> {
            paymentServiceProviders.add(psp);
        });
    }

    @Override
    public List<PaymentServiceProvider> findAllPaymentServiceProviders() {
        return paymentServiceProviders;
    }

    @Override
    public List<PaymentMethodInfo> findAvailablePaymentMethods() {
        return findAllPaymentServiceProviders().stream()
                .flatMap(psp -> psp.getAvailablePaymentMethods().stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMethodInfo> findAvailablePaymentMethods(Function<List<PaymentMethodInfo>, List<PaymentMethodInfo>> filter) {
        return findAllPaymentServiceProviders().stream()
                .flatMap(psp -> psp.getAvailablePaymentMethods(filter).stream())
                .collect(Collectors.toList());
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
                .thenCompose(data -> {
                    if (data.getPayment() == null) {
                        return completedFuture(PaymentTransactionCreationResultBuilder.ofError(
                                format("Payment id=[%s] not found", createData.getPaymentRef())));
                    }

                    return findPaymentServiceProvider(data.getPayment().getPaymentMethodInfo().getPaymentInterface())
                            .provideCreatePaymentTransactionHandler(data.getPayment().getPaymentMethodInfo().getMethod()).apply(data);
                });
    }

    @Override
    public PaymentStatus getPaymentStatus(String ref) {
        return null;
    }

    @Override
    public Optional<PaymentMethodInfo> getPaymentMethodInfo(String interfaceId, String method) {
        return findPaymentServiceProvider(interfaceId).getAvailablePaymentMethods().stream().filter(m -> m.getMethod().equals(method)).findFirst();
    }

    // TODO: what if no PSP is found?
    private PaymentServiceProvider findPaymentServiceProvider(String paymentInterfaceId) {
        return findAllPaymentServiceProviders().stream()
                .filter(psp -> psp.getId().equals(paymentInterfaceId)).findFirst().get();
    }
}
