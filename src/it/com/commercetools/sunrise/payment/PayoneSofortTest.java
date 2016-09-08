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
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayoneSofortTest {

    private SphereClient client;
    private Cart cart;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        this.client = createClient();
        this.cart = createTestCartFromProduct(client, 1);
    }

    @Test
    public void createPaymentCreationMethod() throws ExecutionException, InterruptedException {
        assertThat(client).isNotNull();
        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems().size()).isEqualTo(1);

        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(CreatePaymentDataBuilder.of(client, "PAYONE", "BANK_TRANSFER-SOFORTUEBERWEISUNG", cart, Long.toString(new Date().getTime())).build())
                .toCompletableFuture().get();
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }

    @After
    public void shutdown() throws ExecutionException, InterruptedException {
        removeCart(client, cart);
        client.close();
    }
}
