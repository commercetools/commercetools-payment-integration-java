package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.sunrise.payment.IntegrationTestUtils.*;
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
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        this.client = createClient();
        this.cart = createTestCartFromProduct(client, 2);
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {
        assertPreconditions();

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

        assertPaymentCreation(paymentCreationResult);

        // payment transaction creation is difficult to integration test cause the client side request
        // that provides lots of credit card data is not mockable
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

    private void assertPaymentCreation(PaymentCreationResult paymentCreationResult) {
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getCustom().getFieldAsBoolean(CREDIT_CARD_FORCE_3D_SECURE)).isTrue();
    }
}
