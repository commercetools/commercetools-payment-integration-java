package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.payone.config.PayoneConfiguration;
import com.commercetools.sunrise.payment.payone.config.PayoneConfigurationProvider;
import com.commercetools.sunrise.payment.payone.methods.*;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProvider implements PaymentServiceProvider {

    private PayoneConfiguration configuration;

    public PayonePaymentServiceProvider() {

        configuration = PayoneConfigurationProvider.of().load();
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
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> provideCreatePaymentHandler(final String methodId) throws UnsupportedOperationException {
        switch (methodId) {
            case "CREDIT_CARD": return PayoneCreditCardCreatePaymentMethodProvider.of().create();
            case "WALLET-PAYPAL": return PayonePaypalCreatePaymentMethodProvider.of().create();
            case "BANK_TRANSFER-SOFORTUEBERWEISUNG": return PayoneSofortCreatePaymentMethodProvider.of().create();
            case "BANK_TRANSFER-ADVANCE" : return PayoneBanktransferInAdvanceCreatePaymentProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> provideCreatePaymentTransactionHandler(final String methodId) {
        switch (methodId) {
            case "CREDIT_CARD": return PayoneCreditCardCreatePaymentTransactionMethodProvider.of().create();
            case "WALLET-PAYPAL": return PayonePaypalCreatePaymentTransactionMethodProvider.of().create();
            case "BANK_TRANSFER-SOFORTUEBERWEISUNG": return PayoneSofortCreatePaymentTransactionMethodProvider.of().create();
            case "BANK_TRANSFER-ADVANCE" : return PayoneBanktransferInAdvancePaymentTransactionMethodProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<String, PaymentStatus> provideGetPaymentStatusHandler() {
        return null;
    }
}
