package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.util.IntegrationTestUtils.createClient;
import static com.commercetools.util.IntegrationTestUtils.createTestCartFromProduct;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author mht@dotsource.de
 */
public class NoPaymentMethodTest extends BasePayoneTest {

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(2);
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
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
