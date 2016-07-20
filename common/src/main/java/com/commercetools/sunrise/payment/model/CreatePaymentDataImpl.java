package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.domain.CreatePaymentData;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;

/**
 * Created by mgatz on 7/18/16.
 */
public class CreatePaymentDataImpl implements CreatePaymentData {
    @Override
    public Optional<Customer> getCustomer() {
        return null;
    }

    @Override
    public CreatePaymentData plusCustomer(Customer c) {
        return null;
    }
}
