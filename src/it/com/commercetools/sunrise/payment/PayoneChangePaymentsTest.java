package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.util.IntegrationTestUtils.*;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * author: mht@dotsource.de
 */
public class PayoneChangePaymentsTest extends BasePayoneTest {

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(2);
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {

        // user selected paypal
        final PaymentCreationResult firstPaymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                "WALLET-PAYPAL",
                                cart,
                                Long.toString(new Date().getTime()))
                            .configValue(SUCCESS_URL, "http://google.de")
                            .configValue(ERROR_URL, "http://google.de")
                            .configValue(CANCEL_URL, "http://google.de").build())
                .toCompletableFuture().get();

        cart = updateCart(client, cart);

        // user select prepayment
        final PaymentCreationResult secondPaymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                "BANK_TRANSFER-ADVANCE",
                                cart,
                                Long.toString(new Date().getTime())).build())
                .toCompletableFuture().get();

        cart = updateCart(client, cart);

        // user selects creditcard
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(client, "PAYONE", "CREDIT_CARD", cart, Long.toString(new Date().getTime()))
                                .configValue(CREDIT_CARD_FORCE_3D_SECURE, "true")
                                .configValue(SUCCESS_URL, "http://google.de")
                                .configValue(ERROR_URL, "http://google.de")
                                .configValue(CANCEL_URL, "http://google.de")
                                .configValue(CREDIT_CARD_CARD_DATA_PLACEHOLDER, "placeholder")
                                .configValue(CREDIT_CARD_TRUNCATED_CARD_NUMBER, "truncated")
                                .build())
                .toCompletableFuture().get();

        cart = updateCart(client, cart);

        assertThat(cart.getPaymentInfo().getPayments()).hasSize(3);
        assertThat(cart.getPaymentInfo().getPayments().get(0).getObj().getPaymentMethodInfo().getMethod()).isEqualTo("WALLET-PAYPAL");
        assertThat(cart.getPaymentInfo().getPayments().get(1).getObj().getPaymentMethodInfo().getMethod()).isEqualTo("BANK_TRANSFER-ADVANCE");
        assertThat(cart.getPaymentInfo().getPayments().get(2).getObj().getPaymentMethodInfo().getMethod()).isEqualTo("CREDIT_CARD");
    }
}
