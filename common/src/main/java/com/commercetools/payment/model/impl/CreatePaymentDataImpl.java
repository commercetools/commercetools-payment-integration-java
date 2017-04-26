package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.HttpRequestInfo;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

/**
 * Created by mgatz on 7/18/16.
 */
public class CreatePaymentDataImpl extends PaymentInteractionDataBase implements CreatePaymentData {

    private final String reference;
    private Cart cart;
    @Nullable
    private Customer customer;
    @Nullable
    private HttpRequestInfo httpRequestInfo;
    private PaymentMethodInfo paymentMethodInfo;

    public CreatePaymentDataImpl(SphereClient client,
                                 PaymentMethodInfo paymentMethodInfo,
                                 Cart c,
                                 String reference,
                                 Map<String, String> config,
                                 @Nullable Customer customer,
                                 @Nullable HttpRequestInfo requestInfo) {
        super(client, config);

        this.paymentMethodInfo = paymentMethodInfo;
        this.cart = c;
        this.reference = reference;
        this.customer = customer;
        this.httpRequestInfo = requestInfo;
    }

    @Override
    public Cart getCart() {

        return this.cart;
    }

    @Override
    public String getReference() {
        return reference;
    }

    @Override
    public Optional<Customer> getCustomer() {

        return Optional.ofNullable(customer);
    }

    @Override
    public Optional<HttpRequestInfo> getHttpRequestInfo() {

        return Optional.ofNullable(httpRequestInfo);
    }

    @Override
    public PaymentMethodInfo getPaymentMethodinInfo() {

        return this.paymentMethodInfo;
    }
}
