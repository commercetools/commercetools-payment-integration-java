package com.commercetools.sunrise.payment.domain;

import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A payment service provider provides functions that can be used to handle payment related tasks in
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
     * Create a function that can create a payment for the passed method Id
     * @param methodId the ID of the payment method to be used for the payment object
     * @return a function method creating the payment object for the passed method Id
     */
    Function<CreatePaymentDataProvider, Payment> provideCreatePaymentHandler(String methodId);

    /**
     * Create a function that can create a payment transaction for a payment object
     * and has the ability to handle overriding of configuration values via given key value pairs.
     * @return a function method creating a payment transaction
     */
    BiFunction<Payment, Map<String, String>, Payment> provideCreatePaymentTransactionHandler();

    /**
     * Create a function that can return the payment status for a passed payment reference.
     * @return a function to be executed
     */
    Function<String, PaymentStatus> provideGetPaymentStatusHandler();
}
