package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
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
     * @return an unfiltered list of payment methods this PSP supports
     */
    List<PaymentMethodInfo> getAvailablePaymentMethods();

    /**
     * Create a (filtered) list of payment methods.
     * @param filter a filter function that can be applied to the whole list of payment methods before returning them
     * @return a list of payment methods this PSP supports
     */
    List<PaymentMethodInfo> getAvailablePaymentMethods(@Nullable Function<List<PaymentMethodInfo>, List<PaymentMethodInfo>> filter);

    /**
     * Create a function that can create a payment for the passed method Id
     * @param methodId the ID of the payment method to be used for the payment object
     * @return a function method creating the payment object for the passed method Id
     */
    Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> provideCreatePaymentHandler(String methodId);

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
