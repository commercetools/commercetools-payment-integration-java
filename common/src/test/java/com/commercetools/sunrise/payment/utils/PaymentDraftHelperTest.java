package com.commercetools.sunrise.payment.utils;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.money.MonetaryAmount;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgatz on 7/10/16.
 */
public class PaymentDraftHelperTest {

    @Mock
    private SphereClient mockClient;

    @Mock
    private Cart mockCart;
    private MonetaryAmount testAmount;

    @Before
    public void setup() {
        testAmount = Money.of(5, "EUR");

        mockCart = mock(Cart.class);
        mockClient = mock(SphereClient.class);
        when(mockCart.getTotalPrice()).thenReturn(testAmount);
    }

    @Test
    public void createPaymentDraftBuilder() throws Exception {
        PaymentDraftBuilder builder = PaymentDraftHelper.createPaymentDraftBuilder(
                CreatePaymentDataBuilder.of(mockClient, mockCart).build());

        assertThat(builder.getAmountPlanned()).isEqualTo(testAmount);
    }
}