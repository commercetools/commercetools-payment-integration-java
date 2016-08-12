package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.sunrise.payment.IntegrationTestUtils.*;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * author: mht@dotsource.de
 */
public class PayoneChangePaymentsTest {

    private SphereClient client;
    private Cart cart;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        this.client = createClient();
        this.cart = createTestCartFromProduct(client, 2);
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {
        assertPreconditions();

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
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                "CREDIT_CARD",
                                cart,
                                Long.toString(new Date().getTime()))
                                .configValue(CREDIT_CARD_FORCE_3D_SECURE, "true")
                                .configValue(SUCCESS_URL, "http://google.de")
                                .configValue(ERROR_URL, "http://google.de")
                                .configValue(CANCEL_URL, "http://google.de")
                                .build())
                .toCompletableFuture().get();

        cart = updateCart(client, cart);

        assertThat(cart.getPaymentInfo().getPayments()).hasSize(1);
    }

    @After
    public void shutdown() throws ExecutionException, InterruptedException {
        removeCart(client, cart);
        client.close();
    }

    private void assertPreconditions() {
        assertThat(client).isNotNull();
        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems().size()).isEqualTo(2);
    }
}
