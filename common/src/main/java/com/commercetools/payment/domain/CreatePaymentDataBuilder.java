package com.commercetools.payment.domain;

import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.HttpRequestInfo;
import com.commercetools.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder to build a {@link CreatePaymentDataImpl} object.
 * Created by mgatz on 7/10/16.
 */
public class CreatePaymentDataBuilder {
    private final PaymentMethodInfo paymentMethodInfo;
    private final SphereClient client;
    private final Cart cart;
    private final String reference;
    private Customer customer;
    private HttpRequestInfo httpRequestInfo;
    private Map<String, String> config = new HashMap<>();

    private CreatePaymentDataBuilder(final SphereClient client, final PaymentMethodInfo paymentMethodInfo, final Cart cart, String reference) {
        this.client = client;
        this.cart = cart;
        this.paymentMethodInfo = paymentMethodInfo;
        this.reference = reference;
    }

    public static CreatePaymentDataBuilder of(final SphereClient client,
                                              final String paymentInterface,
                                              final String paymentMethod,
                                              final Cart cart,
                                              final String reference) {
        return new CreatePaymentDataBuilder(
                client,
                PaymentMethodInfoBuilder.of().paymentInterface(paymentInterface).method(paymentMethod).build(),
                cart, reference);
    }

    public static CreatePaymentDataBuilder of(final SphereClient client,
                                              final PaymentMethodInfo paymentMethodInfo,
                                              final Cart cart,
                                              final String reference) {
        return new CreatePaymentDataBuilder(client, paymentMethodInfo, cart, reference);
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
     * Add a named configuration value to this builder.
     * @param key the name of the configuration value
     * @param value the configuration value
     * @return enriched self
     */
    public CreatePaymentDataBuilder configValue(String key, String value) {
        this.config.put(key, value);
        return this;
    }

    /**
     * Create a new instance of {@link CreatePaymentData} using the provided data.
     * @return immutable data object
     */
    public CreatePaymentData build() {
        return new CreatePaymentDataImpl(
                this.client,
                this.paymentMethodInfo,
                this.cart,
                this.reference,
                this.config,
                this.customer,
                this.httpRequestInfo);
    }

}
