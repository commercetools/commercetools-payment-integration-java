package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;

/**
 * Provides a wrapper for all data that could possibly be needed by a {@link PaymentServiceProvider} implementation
 * to create a new payment object.
 *
 * Created by mgatz on 7/18/16.
 */
public interface CreatePaymentData extends PaymentInteractionData {

    /**
     * @return the cart object that is needed to create a payment for
     */
    Cart getCart();

    /**
     * @return the attached customer
     */
    Optional<Customer> getCustomer();

    /**
     * @return the HTTP client request data wrapper
     */
    Optional<HttpRequestInfo> getHttpRequestInfo();
}
