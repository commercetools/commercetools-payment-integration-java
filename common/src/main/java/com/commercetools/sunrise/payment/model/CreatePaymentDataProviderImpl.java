package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataProvider;
import io.sphere.sdk.customers.Customer;

import java.util.Optional;

/**
 * Created by mgatz on 7/18/16.
 */
public class CreatePaymentDataProviderImpl implements CreatePaymentDataProvider {
    @Override
    public Optional<Customer> getCustomer() {
        return null;
    }

    @Override
    public CreatePaymentDataProvider plusCustomer(Customer c) {
        return null;
    }
}
