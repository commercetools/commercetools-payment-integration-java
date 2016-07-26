package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.HttpRequestInfo;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.payments.PaymentMethodInfo;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 7/18/16.
 */
public class CreatePaymentDataImpl extends PaymentInteractionDataBase implements CreatePaymentData {

    private Cart cart;
    @Nullable
    private Customer customer;
    @Nullable
    private HttpRequestInfo httpRequestInfo;

    public CreatePaymentDataImpl(SphereClient client, PaymentMethodInfo paymentMethodInfo, Cart c, @Nullable Customer customer, @Nullable HttpRequestInfo requestInfo) {
        super(client, paymentMethodInfo);

        this.cart = c;
        this.customer = customer;
        this.httpRequestInfo = requestInfo;
    }

    @Override
    public Cart getCart() {

        return this.cart;
    }

    @Override
    public Optional<Customer> getCustomer() {

        return Optional.ofNullable(customer);
    }

    @Override
    public Optional<HttpRequestInfo> getHttpRequestInfo() {

        return Optional.ofNullable(httpRequestInfo);
    }
}
