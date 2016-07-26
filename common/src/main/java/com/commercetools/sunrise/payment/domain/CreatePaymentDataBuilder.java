package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.HttpRequestInfo;
import com.commercetools.sunrise.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;

/**
 * A builder to build a {@link CreatePaymentDataImpl} object.
 * Created by mgatz on 7/10/16.
 */
public class CreatePaymentDataBuilder {
    private final PaymentMethodInfo paymentMethodInfo;
    private final SphereClient client;
    private final Cart cart;
    private Customer customer;
    private HttpRequestInfo httpRequestInfo;

    private CreatePaymentDataBuilder(final SphereClient client,final PaymentMethodInfo paymentMethodInfo,final Cart cart) {
        this.client = client;
        this.cart = cart;
        this.paymentMethodInfo = paymentMethodInfo;
    }

    public static CreatePaymentDataBuilder of(final SphereClient client,
                                              final String paymentInterface,
                                              final String paymentMethod,
                                              final Cart cart) {
        return new CreatePaymentDataBuilder(
                client,
                PaymentMethodInfoBuilder.of().paymentInterface(paymentInterface).method(paymentMethod).build(),
                cart);
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
        return new CreatePaymentDataImpl(client, paymentMethodInfo, cart, customer, httpRequestInfo);
    }
}
