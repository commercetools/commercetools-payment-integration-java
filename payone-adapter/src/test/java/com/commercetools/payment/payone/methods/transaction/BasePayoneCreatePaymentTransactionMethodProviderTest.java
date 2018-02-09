package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.model.impl.CreatePaymentTransactionDataImpl;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.TransactionType;
import io.sphere.sdk.payments.commands.PaymentUpdateCommand;
import io.sphere.sdk.payments.commands.updateactions.AddTransaction;
import io.sphere.sdk.types.CustomFields;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;
import java.util.List;

import static io.sphere.sdk.payments.TransactionState.INITIAL;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Inherited classes must be run with {@code @RunWith(MockitoJUnitRunner.class)}
 */
public class BasePayoneCreatePaymentTransactionMethodProviderTest {

    protected PayoneCreatePaymentTransactionMethodBase transactionMethod;

    @Mock
    protected Payment payment;

    @Mock
    protected CustomFields customFields;

    @Mock
    protected SphereClient sphereClient;


    /**
     * Stub {@link #customFields} to the {@link #payment}.
     */
    protected void applyCustomFieldStubbing() {
        when(payment.getCustom()).thenReturn(customFields);
    }

    /**
     * Stub {@code paymentInterface} and {@code amount} to the {@link #payment}.
     */
    protected void applyPaymentStubbing(@Nullable PaymentMethodInfo paymentInterface, @Nullable MonetaryAmount amount) {
        when(payment.getAmountPlanned()).thenReturn(amount);
        when(payment.getPaymentMethodInfo()).thenReturn(paymentInterface);
    }

    /**
     * Without real Sphere and Payone service calls, verify that these services would be called
     * with expected add transaction update actions.
     */
    protected void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(TransactionType transactionType,
                                                                                                 MonetaryAmount expectedAmount) {
        // mock responses
        CreatePaymentTransactionDataImpl cptd = CreatePaymentTransactionDataBuilder.of(sphereClient, "test-payment-ref-id")
                .setTransactionType(transactionType)
                .build();
        cptd.setPayment(payment);

        Payment paymentToReturn = mock(Payment.class);
        when(sphereClient.execute(any(PaymentUpdateCommand.class))).thenReturn(completedFuture(paymentToReturn));

        // make the actual call
        transactionMethod.create().apply(cptd).toCompletableFuture().join();

        // verify arguments passed to the call
        ArgumentCaptor<PaymentUpdateCommand> paymentUpdateCommandCaptor = ArgumentCaptor.forClass(PaymentUpdateCommand.class);
        verify(sphereClient).execute(paymentUpdateCommandCaptor.capture());
        List<? extends UpdateAction<Payment>> addTransactionActions = paymentUpdateCommandCaptor.getValue().getUpdateActions().stream()
                .filter(a -> a instanceof AddTransaction)
                .collect(toList());

        // only 1 AddTransaction is expected with following type/state/amount:
        assertThat(addTransactionActions.size()).isEqualTo(1);
        AddTransaction tr = (AddTransaction) addTransactionActions.get(0);
        assertThat(tr.getTransaction().getType()).isEqualByComparingTo(transactionType);
        assertThat(tr.getTransaction().getAmount()).isEqualByComparingTo(expectedAmount);

        // since http://dev.commercetools.com/release-notes.html#release-notes---commercetools-platform---version-release-29-september-2017
        // all transactions should be created with explicit initial state
        assertThat(tr.getTransaction().getState()).isEqualByComparingTo(INITIAL);
    }

    public void handleSuccessfulServiceCall_default() throws Exception {
        PaymentTransactionCreationResult ptcr = transactionMethod.handleSuccessfulServiceCall(payment);
        assertThat(ptcr).isNotNull();
        assertThat(ptcr.getRelatedPaymentObject()).contains(payment);
        assertThat(ptcr.getException()).isNotPresent();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);

        assertThat(ptcr.getHandlingTask()).isNotNull();
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(ptcr.getHandlingTask().getRedirectUrl()).isNotPresent();

        assertThat(ptcr.getMessage()).isNotPresent();
    }
}
