package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Provides a wrapper for all data that could possibly be needed by a {@link PaymentServiceProvider} implementation
 * to create a new payment object.
 *
 * Created by mgatz on 7/18/16.
 */
public interface CreatePaymentData extends InteractionData {

    static CreatePaymentData of(SphereClient client, Cart c) {

        return new CreatePaymentDataImpl(client, c);
    }

    /**
     * @return the cart object that is needed to create a payment for
     */
    Cart getCart();

    /**
     * @return the attached customer
     */
    @Nullable
    Optional<Customer> getCustomer();

    /**
     * Add a customer object to this data provider instance.
     * @param c the customer object to be added
     * @return enriched self
     */
    CreatePaymentData withCustomer(Customer c);

    /**
     * @return the HTTP client request data wrapper
     */
    Optional<HttpRequestInfo> getHttpRequestInfo();

    /**
     * Add a {@link HttpRequestInfo} object.
     * @param hri
     * @return enriched self
     */
    CreatePaymentData withHttpRequestInfo(HttpRequestInfo hri);
}
