package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.Monetary;
import java.util.concurrent.ExecutionException;

import static com.commercetools.util.IntegrationTestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class BasePayoneTest {
    protected SphereClient client;
    protected Cart cart;

    protected void setup(int cartSize) throws ExecutionException, InterruptedException {
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        this.client = createClient();
        this.cart = createTestCartFromProduct(client, cartSize);

        assertPreconditions(cartSize);
    }

    protected void shutdown() throws ExecutionException, InterruptedException {
        removeCart(client, cart);
        client.close();
    }

    protected void assertPreconditions(int cartSize) {
        assertThat(client).isNotNull();
        assertThat(cart).isNotNull();
        assertThat(cart.getLineItems().size()).isEqualTo(cartSize);
    }

    protected void assertPaymentObjectCreation(PaymentCreationResult pcr) {
        assertThat(pcr).isNotNull();
        assertThat(pcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(pcr.getHandlingTask()).isNotNull();
        assertThat(pcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(pcr.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(pcr.getRelatedPaymentObject().get().getTransactions().size()).isEqualTo(0);
        assertThat(pcr.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }

    protected void assertPaymentTransactionObjectCreation(PaymentTransactionCreationResult ptcr) {
        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getOperationResult())
                .withFailMessage("Transaction operation failed!%nMessage: [%s]%nException: [%s]",
                        ptcr.getMessage(), ptcr.getException())
                .isEqualTo(OperationResult.SUCCESS);

        assertThat(ptcr.getRelatedPaymentObject()).isPresent();
        assertThat(ptcr.getRelatedPaymentObject().get().getTransactions().size()).isGreaterThan(0); // at least one has to be there
    }
}
