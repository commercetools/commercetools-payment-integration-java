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

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
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
        this.cart = IntegrationTestUtils.createTestCartFromProduct(client, 2);
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {
        assertPreconditions();

        // user selected paypal
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
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

        assertPaymentObjectCreation(paymentCreationResult);

        // user clicked "buy now" -> create transaction, trigger handle payment, return updated payment object
        PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, "https://coeur-payment-stage.ct-app.com/commercetools/handle/payments/")
                                .build())
                .toCompletableFuture().get();

        assertPaymentTransactionObjectCreation(paymentTransactionCreationResult);
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

    private void assertPaymentObjectCreation(PaymentCreationResult pcr) {
        assertThat(pcr).isNotNull();
        assertThat(pcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(pcr.getHandlingTask()).isNotNull();
        assertThat(pcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(pcr.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(pcr.getRelatedPaymentObject().get().getTransactions().size()).isEqualTo(0);
        assertThat(pcr.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }

    private void assertPaymentTransactionObjectCreation(PaymentTransactionCreationResult ptcr) {
        assertThat(ptcr).isNotNull();
        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(ptcr.getRelatedPaymentObject()).isPresent();
        assertThat(ptcr.getRelatedPaymentObject().get().getTransactions().size()).isGreaterThan(0); // at least one has to be there
    }
}
