package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.HttpRequestInfo;
import com.commercetools.sunrise.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;

/**
 * Created by mgatz on 7/10/16.
 */
public class CreatePaymentDataBuilder {
    private SphereClient client;
    private Cart cart;
    private Customer customer;
    private HttpRequestInfo httpRequestInfo;

    private CreatePaymentDataBuilder(SphereClient client, Cart cart) {
        this.client = client;
        this.cart = cart;
    }

    public static CreatePaymentDataBuilder of(SphereClient client, Cart cart) {
        return new CreatePaymentDataBuilder(client, cart);
    }

    /**
     * Add a customer object.
     * @param c the customer object to be added
     * @return enriched self
     */
    CreatePaymentDataBuilder withCustomer(Customer c) {
        this.customer = c;
        return this;
    }


    /**
     * Add a {@link HttpRequestInfo} object.
     * @param hri
     * @return enriched self
     */
    CreatePaymentDataBuilder withHttpRequestInfo(HttpRequestInfo hri) {
        this.httpRequestInfo = hri;
        return this;
    }

    /**
     * Create a new instance of {@link CreatePaymentData} using the provided data.
     * @return immutable data object
     */
    public CreatePaymentData build() {
        return new CreatePaymentDataImpl(client, cart, customer, httpRequestInfo);
    }
}
