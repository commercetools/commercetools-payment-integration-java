package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 8/1/16.
 */
public class PaymentServiceAdapterTest {
    @Test
    public void getEnabledPaymentMethods() {
        List<PaymentMethodInfo> enabledMethods = PaymentAdapterService.of().findAvailablePaymentMethods();

        assertThat(enabledMethods.size()).isEqualTo(3);
    }
}
