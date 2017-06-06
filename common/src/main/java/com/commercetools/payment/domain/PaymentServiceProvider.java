package com.commercetools.payment.domain;

import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletionStage;
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
     * @param methodId the ID of the payment method to be used for the payment transaction object
     * @return a function method creating a payment transaction
     */
    Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> provideCreatePaymentTransactionHandler(String methodId);

}
