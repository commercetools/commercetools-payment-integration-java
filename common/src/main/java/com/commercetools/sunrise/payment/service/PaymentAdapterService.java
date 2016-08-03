package com.commercetools.sunrise.payment.service;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Defines the methods that the shop could call to handle its payment.
 * The shop should never directly call a {@link PaymentServiceProvider} implementation.
 *
 * Created by mgatz on 7/18/16.
 */
public interface PaymentAdapterService {

    /**
     * @return the default instance of the payment adapter service
     */
    static PaymentAdapterService of() {
        return new PaymentAdapterServiceImpl();
    }
    /**
     * Find all payment services (implementing {@link PaymentServiceProvider} on the classpath and return a list of their
     * service entry points.
     * @return list of payment service implementation entry points
     */
     List<PaymentServiceProvider> findAllPaymentServiceProviders();

    /**
     * Get a list of available payment methods collected from all available {@link PaymentServiceProvider} instances {@link #findAllPaymentServiceProviders()}
     * @return list of payment method objects
     */
    List<PaymentMethodInfo> findAvailablePaymentMethods(); // TODO: add parameters possibly required

    /**
     * Creates a new payment object at the CTP and theirby starts a new payment transaction workflow.
     * Possibly existing payments will be cancelled but not deleted.
     * @param data the wrapper object for all possibly needed data
     */
    CompletionStage<PaymentCreationResult> createPayment(CreatePaymentData data);

    /**
     * Create a new payment transaction for the payment with the passed reference.
     * @param data the wrapper object for all possibly needed data
     */
    CompletionStage<PaymentTransactionCreationResult> createPaymentTransaction(CreatePaymentTransactionData data);

    /**
     * Get the status of the payment object referenced by the passed parameter.
     * @param ref the reference of to the payment the status should be returned for
     * @return the status of the referenced payment object
     */
    PaymentStatus getPaymentStatus(String ref); // TODO: check if parameter is sufficient

    /**
     * Get the full {@link PaymentMethodInfo} object from the configuration.
     * @param interfaceId
     * @param method
     * @return
     */
    Optional<PaymentMethodInfo> getPaymentMethodInfo(String interfaceId, String method);
}
