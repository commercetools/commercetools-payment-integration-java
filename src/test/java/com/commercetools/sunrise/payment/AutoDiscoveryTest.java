package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.nopsp.NoPaymentServiceProvider;
import com.commercetools.sunrise.payment.payone.PayonePaymentServiceProvider;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/19/16.
 */
public class AutoDiscoveryTest {
    @Test
    public void findPayoneImplementation() {
        List<PaymentServiceProvider> providers = PaymentAdapterService.of().findAllPaymentServiceProviders();
        assertThat(providers.size()).isEqualTo(2); // there is only one service implementation to be discovered
        assertThat(providers.get(0)).isInstanceOfAny(PayonePaymentServiceProvider.class, NoPaymentServiceProvider.class);
        assertThat(providers.get(1)).isInstanceOfAny(PayonePaymentServiceProvider.class, NoPaymentServiceProvider.class);
    }
}
