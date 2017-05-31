package com.commercetools.payment.payone;

import com.commercetools.payment.domain.PaymentServiceProvider;
import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.payone.config.PayoneConfiguration;
import com.commercetools.payment.payone.config.PayoneConfigurationProvider;
import com.commercetools.payment.payone.methods.*;
import com.commercetools.payment.payone.methods.transaction.*;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.commercetools.payment.methods.PaymentMethodKeys.*;

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
            case CREDIT_CARD: return PayoneCreditCardCreatePaymentMethodProvider.of().create();
            case WALLET_PAYPAL: return PayonePaypalCreatePaymentMethodProvider.of().create();
            case BANK_TRANSFER_ADVANCE : return PayoneBanktransferInAdvanceCreatePaymentProvider.of().create();
            case BANK_TRANSFER_SOFORTUEBERWEISUNG:
            case BANK_TRANSFER_POSTFINANCE_EFINANCE:
            case BANK_TRANSFER_POSTFINANCE_CARD:
                return PayoneBankTransferCreatePaymentMethodProvider.of().create();
            case INVOICE_KLARNA: return PayoneKlarnaCreatePaymentMethodProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> provideCreatePaymentTransactionHandler(final String methodId) {
        switch (methodId) {
            case CREDIT_CARD: return PayoneCreditCardCreatePaymentTransactionMethodProvider.of().create();
            case WALLET_PAYPAL: return PayonePaypalCreatePaymentTransactionMethodProvider.of().create();
            case BANK_TRANSFER_ADVANCE : return PayoneBanktransferInAdvancePaymentTransactionMethodProvider.of().create();
            case BANK_TRANSFER_SOFORTUEBERWEISUNG:
            case BANK_TRANSFER_POSTFINANCE_EFINANCE :
            case BANK_TRANSFER_POSTFINANCE_CARD:
                return PayoneBankTransferCreateTransactionMethodProvider.of().create();
            case INVOICE_KLARNA: return PayoneKlarnaCreatePaymentTransactionMethodProvider.of().create();
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Function<String, PaymentStatus> provideGetPaymentStatusHandler() {
        return null;
    }
}
