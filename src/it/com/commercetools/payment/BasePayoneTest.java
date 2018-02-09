package com.commercetools.payment;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.Transaction;
import io.sphere.sdk.payments.TransactionState;
import io.sphere.sdk.payments.TransactionType;
import io.sphere.sdk.types.CustomFields;
import io.sphere.sdk.utils.MoneyImpl;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.money.Monetary;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REFERENCE;
import static com.commercetools.util.IntegrationTestUtils.*;
import static io.sphere.sdk.payments.TransactionState.INITIAL;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

public class BasePayoneTest {
    protected BlockingSphereClient client;
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

    /**
     * Assert default payment creation results (operation result, handle task action, price etc)
     *
     * @param pcr       {@link PaymentCreationResult} to validate.
     * @param reference reference (order number) custom field.
     */
    protected void assertPaymentObjectCreation(PaymentCreationResult pcr, String reference) {
        assertThat(pcr).isNotNull();
        assertThat(pcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(pcr.getHandlingTask()).isNotNull();
        assertThat(pcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(pcr.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(pcr.getRelatedPaymentObject().get().getTransactions().size()).isEqualTo(0);
        assertThat(pcr.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());

        CustomFields customFields = getCustomFields(pcr);

        assertThat(customFields.getFieldAsString(REFERENCE)).isEqualTo(reference);
    }

    @Nonnull
    protected static CustomFields getCustomFields(PaymentCreationResult pcr) {
        return pcr.getRelatedPaymentObject().map(Payment::getCustom)
                .orElseThrow(() -> new IllegalStateException("Payment custom fields are empty"));
    }

    /**
     * Assert default transaction creation results (operation result, Related Payment Objects)
     *
     * @param ptcr         {@link PaymentTransactionCreationResult} to validate.
     * @param expectedType expected transaction type from {@code payone.json} project settings.
     */
    protected void assertPaymentTransactionObjectCreation(PaymentTransactionCreationResult ptcr,
                                                          TransactionType expectedType,
                                                          TransactionState expectedState) {
        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getOperationResult())
                .withFailMessage("Transaction operation failed!%nMessage: [%s]%nException: [%s]",
                        ptcr.getMessage(), ptcr.getException())
                .isEqualTo(OperationResult.SUCCESS);

        Payment payment = ptcr.getRelatedPaymentObject().orElse(null);
        assertThat(payment).isNotNull();
        List<Transaction> transactions = payment.getTransactions();
        final int size = transactions.size();
        assertThat(size).isGreaterThan(0); // at least one has to be there
        Transaction transaction = transactions.get(size - 1);
        assertThat(transaction.getType()).isEqualTo(expectedType);

        // PaymentAdapterService.createPaymentTransaction() creates transaction and immediately handles the payment,
        // thus after execution the status switches from INITIAL to some other
        assertThat(transaction.getState()).isNotEqualByComparingTo(INITIAL);
        assertThat(transaction.getState()).isEqualTo(expectedState);
    }

    /**
     * Create an order (reference) number for test purpose.
     *
     * @param suffix suffix for the order number (for instance, payment name)
     * @return system nano-seconds value concatenated with optional "-" and {@code suffix}.
     * The full result is truncated to 20 characters (Payone requirements).
     */
    protected static String generateTestPayoneReference(@Nullable String suffix) {
        return StringUtils.left(Long.toString(System.nanoTime()) + ofNullable(suffix)
                        .map(str -> "-" + str)
                        .orElse(""),
                20); // Payone reference length limit
    }
}
