package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataProvider;
import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderImpl implements PaymentServiceProvider {

    private static final String PAYMENT_SERVICE_ID = "payone";

    @Override
    public String getId() {
        return PAYMENT_SERVICE_ID;
    }

    @Override
    public List<PaymentMethodInfo> getAvailablePaymentMethods() {

        return null;
    }

    @Override
    public Function<CreatePaymentDataProvider, Payment> provideCreatePaymentHandler(String methodId) {
        return null;
    }

    @Override
    public BiFunction<Payment, Map<String, String>, Payment> provideCreatePaymentTransactionHandler() {
        return null;
    }

    @Override
    public Function<String, PaymentStatus> provideGetPaymentStatusHandler() {
        return null;
    }
}
