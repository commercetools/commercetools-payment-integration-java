package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mgatz on 7/19/16.
 */
public class AutoDiscoveryTest {
    @Test
    public void findPayoneImplementation() {
        List<PaymentServiceProvider> providers = PaymentAdapterService.of().findAllPaymentServiceProviders();
        assertThat(providers.size(), is(1)); // there is only one service implementation to be discovered
    }
}
