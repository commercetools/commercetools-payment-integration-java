package com.commercetools.payment.service;

import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class PaymentServiceAdapterTest {

    @Test
    public void of_returnsDefaultPaymentAdapterServiceImpl() throws Exception {
        assertThat(PaymentAdapterService.of()).isInstanceOf(PaymentAdapterServiceImpl.class);
    }

    @Test
    public void getEnabledPaymentMethods_returnsAllRegisteredMethods() {
        List<PaymentMethodInfo> enabledMethods = PaymentAdapterService.of().findAvailablePaymentMethods();
        assertThat(enabledMethods.size()).isEqualTo(9);
    }
}
