package com.commercetools.sunrise.payment;

import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

public interface Payment { // TODO change name to something less generic

    List<PaymentMethodInfo> getAvailablePaymentMethods(); // TODO decide parameters

    void createPayment(); // TODO decide return type and parameters

    void getPaymentStatus(); // TODO decide return type and parameters
}
