package com.commercetools.sunrise.payment.domain;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A payment service provider provides functions and consumers that can be used to handle payment related tasks in
 * a PSP specific way.
 * @author mgatz
 */
public interface PaymentServiceProvider {

    /**
     * @return a unique identifier of the payment service provider
     */
    String getId();

    /**
     * @return a list of payment methods this PSP supports
     */
    List<PaymentMethodInfo> getAvailablePaymentMethods(); // TODO decide parameters

    /**
     * Create a consumer that can create a payment for the passed method Id
     * @param methodId the ID of the payment method to be used for the payment object
     * @return a consumer method creating the payment object for the passed method Id
     */
    Consumer<CreatePaymentDataProvider> provideCreatePaymentHandler(String methodId);

    /**
     * Create a function that can return the payment status for a passed payment reference.
     * @return a function to be executed
     */
    Function<String, PaymentStatus> provideGetPaymentStatusHandler();
}
