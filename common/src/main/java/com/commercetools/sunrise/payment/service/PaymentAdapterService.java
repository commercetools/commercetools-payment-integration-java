package com.commercetools.sunrise.payment.service;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataProvider;
import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentStatus;

import java.util.List;
import java.util.Map;

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
     * @param methodID the ID of the selected payment method
     * @param data the wrapper object for all possibly needed data
     */
    void createPayment(String methodID, CreatePaymentDataProvider data);

    /**
     * Create a new payment transaction for the payment with the passed reference.
     * @param paymentRef the reference for the payment object
     */
    void createPaymentTransaction(String paymentRef);

    /**
     * Create a new payment transaction for the payment with the passed refernce and allow overriding of default
     * configuration values passed as a map.
     * @param paymentRef the reference for the payment object
     * @param configData a map that can hold values overriding defaults
     */
    void createPaymentTransaction(String paymentRef, Map<String, String> configData);

    /**
     * Create a new payment transaction and a payment object in one action.
     * @param data the wrapper object for all possibly needed data
     */
    void createPaymentTransaction(CreatePaymentDataProvider data);

    /**
     * Create  new payment transaction and a payment object in one action and allow overriding of configuration values.
     * @param data the wrapper object for all possibly needed data
     * @param configData a map that can hold values overriding defaults
     */
    void createPaymentTransaction(CreatePaymentDataProvider data, Map<String, String> configData);

    /**
     * Get the status of the payment object referenced by the passed parameter.
     * @param ref the reference of to the payment the status should be returned for
     * @return the status of the referenced payment object
     */
    PaymentStatus getPaymentStatus(String ref); // TODO: check if parameter is sufficient
}
