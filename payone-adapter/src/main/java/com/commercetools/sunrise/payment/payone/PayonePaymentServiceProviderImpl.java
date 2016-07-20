package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.domain.CreatePaymentData;
import com.commercetools.sunrise.payment.domain.PaymentCreationResult;
import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.utils.PaymentPropertiesLoadingHelper;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderImpl implements PaymentServiceProvider {

    private PaymentPropertiesLoadingHelper propertiesLoadingHelper;

    public PayonePaymentServiceProviderImpl() {
        propertiesLoadingHelper = PaymentPropertiesLoadingHelper.createFromResource("methods/payone.properties");
    }

    @Override
    public String getId() {
        return propertiesLoadingHelper.getProperty("methods.interface");
    }

    @Override
    public List<PaymentMethodInfo> getAvailablePaymentMethods() {
        return propertiesLoadingHelper.getAvaiableMethodIds().stream().map(id -> propertiesLoadingHelper.getMethodInfo(id)).collect(Collectors.toList());
    }

    @Override
    public Function<CreatePaymentData, PaymentCreationResult> provideCreatePaymentHandler(String methodId) {
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
