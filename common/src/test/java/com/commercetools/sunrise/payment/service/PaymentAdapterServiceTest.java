package com.commercetools.sunrise.payment.service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/18/16.
 */
public class PaymentAdapterServiceTest {

    @Test
    public void ofReturnsDefaultInstance() {
        assertThat(PaymentAdapterService.of()).isInstanceOf(PaymentAdapterServiceImpl.class);
    }
}