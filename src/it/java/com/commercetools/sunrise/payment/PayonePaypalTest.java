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

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayonePaypalTest {

    private SphereClient client;
    private Cart cart;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        this.client = IntegrationTestUtils.createClient();
        this.cart = IntegrationTestUtils.createTestCartFromProduct(client, IntegrationTestUtils.getProduct(client));
    }

    @Test
    public void createPaymentCreationMethod() throws ExecutionException, InterruptedException {
        assertPreconditions();

        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(CreatePaymentDataBuilder.of(client, "PAYONE", "WALLET-PAYPAL", cart).build())
                .toCompletableFuture().get();

        assertPaymentObjectCreation(paymentCreationResult);
    }


    @After
    public void shutdown() throws ExecutionException, InterruptedException {
        IntegrationTestUtils.removeCart(client, cart);
        client.close();
    }

    private void assertPreconditions() {
        assertThat(client).isNotNull();
        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems().size()).isEqualTo(1);
    }

    private void assertPaymentObjectCreation(PaymentCreationResult paymentCreationResult) {
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getCreatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getCreatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }
}
