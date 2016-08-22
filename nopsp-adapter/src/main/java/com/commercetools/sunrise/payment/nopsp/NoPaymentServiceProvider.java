package com.commercetools.sunrise.payment.nopsp;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.*;
import com.commercetools.sunrise.payment.nopsp.config.NoPaymentServiceConfiguration;
import com.commercetools.sunrise.payment.nopsp.config.NoPaymentServiceConfigurationProvider;
import com.commercetools.sunrise.payment.nopsp.methods.FreeCreatePaymentMethodProvider;
import com.commercetools.sunrise.payment.nopsp.methods.FreeCreatePaymentTransactionMethodProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * For all Payment Methods that does not need a Payment Service Provider. F.I. Free payment.
 * @author mht@dotsource.de
 */
public class NoPaymentServiceProvider implements PaymentServiceProvider {
    NoPaymentServiceConfiguration configuration;

    public NoPaymentServiceProvider() {
        configuration = NoPaymentServiceConfigurationProvider.of().load();
    }

    @Override
    public String getId() {
        return configuration.getInterfaceId();
    }

    @Override
    public List<PaymentMethodInfo> getAvailablePaymentMethods() {
        return getAvailablePaymentMethods(null);
    }

    @Override
    public List<PaymentMethodInfo> getAvailablePaymentMethods(@Nullable Function<List<PaymentMethodInfo>, List<PaymentMethodInfo>> filter) {
        List<PaymentMethodInfo> unfiltered = configuration.getEnabledMethods().stream().map(methodId -> configuration.getMethodInfo(methodId)).collect(Collectors.toList());

            return filter != null
                    ? filter.apply(unfiltered)
                    : unfiltered;
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> provideCreatePaymentHandler(String methodId) {
        switch (methodId) {
            case "FREE": return FreeCreatePaymentMethodProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> provideCreatePaymentTransactionHandler(String methodId) {
        switch (methodId) {
            case "FREE": return FreeCreatePaymentTransactionMethodProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<String, PaymentStatus> provideGetPaymentStatusHandler() {
        return null;
    }
}
