package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.sunrise.payment.IntegrationTestUtils.createClient;
import static com.commercetools.sunrise.payment.IntegrationTestUtils.createTestCartFromProduct;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.CREDIT_CARD_CARD_DATA_PLACEHOLDER;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.CREDIT_CARD_TRUNCATED_CARD_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mht@dotsource.de
 */
public class NoPaymentMethodTest {

    private SphereClient client;
    private Cart cart;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        this.client = createClient();
        this.cart = createTestCartFromProduct(client, 2);
    }

    @Test
    public void testPaymentCreation() throws ExecutionException, InterruptedException {

        final PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(client, "INTERNAL", "FREE", cart, Long.toString(new Date().getTime()))
                                .build())
                .toCompletableFuture().get();

        assertThat(paymentCreationResult.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject()).isPresent();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getPaymentMethodInfo().getPaymentInterface()).isEqualTo("INTERNAL");
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }

    @Test
    public void testTransactionCreation() throws ExecutionException, InterruptedException {

        final PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(client, "INTERNAL", "FREE", cart, Long.toString(new Date().getTime()))
                                .build())
                .toCompletableFuture().get();

        final String referenceId = paymentCreationResult.getRelatedPaymentObject().get().getId();

        final PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder.of(client, referenceId).build()
                        ).toCompletableFuture().get();

        assertThat(paymentTransactionCreationResult.getOperationResult()).isEqualTo((OperationResult.SUCCESS));
        assertThat(paymentTransactionCreationResult.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
    }


}
