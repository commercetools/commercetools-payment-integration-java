package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayoneCreditCardTest {

    private SphereClient client;
    private Cart cart;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        this.client = IntegrationTestUtils.createClient();
        this.cart = IntegrationTestUtils.createTestCartFromProduct(client, 2);
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {
        assertPreconditions();

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

        assertPaymentCreation(paymentCreationResult);

        // do javascript JSONP call

        // update payment object and create transaction

        /*
        PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder.of(client, paymentCreationResult.getRelatedPaymentObject().get().getId()).build())
                .toCompletableFuture().get();
                */
    }

    @After
    public void shutdown() throws ExecutionException, InterruptedException {
        IntegrationTestUtils.removeCart(client, cart);
        client.close();
    }

    private void assertPreconditions() {
        assertThat(client).isNotNull();
        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems().size()).isEqualTo(2);
    }

    private void assertPaymentCreation(PaymentCreationResult paymentCreationResult) {
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getCustom().getFieldAsBoolean(CREDIT_CARD_FORCE_3D_SECURE)).isTrue();
    }
}
