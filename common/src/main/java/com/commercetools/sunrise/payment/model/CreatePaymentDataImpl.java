package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.domain.CreatePaymentData;
import com.commercetools.sunrise.payment.domain.HttpRequestInfo;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 7/18/16.
 */
public class CreatePaymentDataImpl extends InteractionDataBase implements CreatePaymentData {

    private Cart cart;
    @Nullable
    private Customer customer;
    @Nullable
    private HttpRequestInfo httpRequestInfo;

    public CreatePaymentDataImpl(SphereClient client, Cart c) {
        super(client);

        this.cart = c;
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
    public CreatePaymentData withCustomer(Customer c) {
        this.customer = c;
        return this;
    }

    @Override
    public Optional<HttpRequestInfo> getHttpRequestInfo() {
        return Optional.ofNullable(httpRequestInfo);
    }

    @Override
    public CreatePaymentData withHttpRequestInfo(HttpRequestInfo hri) {
        this.httpRequestInfo = hri;
        return this;
    }
}
