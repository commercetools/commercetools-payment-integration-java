package com.commercetools.payment.methods;

import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.model.impl.CreatePaymentTransactionDataImpl;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionDraft;
import io.sphere.sdk.payments.commands.PaymentUpdateCommand;
import io.sphere.sdk.payments.commands.updateactions.AddTransaction;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.money.MonetaryAmount;
import java.util.HashMap;
import java.util.Map;

import static io.sphere.sdk.payments.TransactionState.INITIAL;
import static io.sphere.sdk.payments.TransactionType.AUTHORIZATION;
import static io.sphere.sdk.payments.TransactionType.CHARGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreatePaymentTransactionMethodBaseTest {

    @Mock
    private CreatePaymentTransactionMethodBase createPaymentTransactionMethod;

    @Mock
    private SphereClient sphereClient;

    @Mock
    private Payment payment;

    private CreatePaymentTransactionData transactionData;

    private MonetaryAmount amount42;

    @Before
    public void setUp() {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("key1", "value1");
        configMap.put("key2", "value2");

        amount42 = Money.of(42.42, "EUR");

        when(payment.getAmountPlanned()).thenReturn(amount42);

        transactionData = new CreatePaymentTransactionDataImpl(sphereClient, "mock-ref", configMap);
        transactionData.setPayment(payment);

        doCallRealMethod().when(createPaymentTransactionMethod).createPaymentTransaction(any(), any());
        doCallRealMethod().when(createPaymentTransactionMethod).createTransactionDraftBuilder(any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void createPaymentTransaction_sendsPaymentUpdateCommandWithInitialTransactionState() {
        createPaymentTransactionMethod.createPaymentTransaction(transactionData, AUTHORIZATION);

        ArgumentCaptor<PaymentUpdateCommand> updateCommand = ArgumentCaptor.forClass(PaymentUpdateCommand.class);
        verify(sphereClient, times(1)).execute(updateCommand.capture());

        assertThat(updateCommand.getValue().getUpdateActions())
                .filteredOn(action -> action instanceof AddTransaction)
                .extracting(action -> ((AddTransaction) action).getTransaction().getState())
                .containsExactly(INITIAL);
    }

    @Test
    public void createTransactionDraftBuilder_simpleCase() {
        TransactionDraft transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, AUTHORIZATION).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getTimestamp()).isNull();
        assertThat(transactionDraft.getInteractionId()).isNull();
        assertThat(transactionDraft.getAmount()).isEqualByComparingTo(Money.of(42.42, "EUR"));
        assertThat(transactionDraft.getType()).isEqualByComparingTo(AUTHORIZATION);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);

        transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, CHARGE).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getTimestamp()).isNull();
        assertThat(transactionDraft.getInteractionId()).isNull();
        assertThat(transactionDraft.getAmount()).isEqualByComparingTo(Money.of(42.42, "EUR"));
        assertThat(transactionDraft.getType()).isEqualByComparingTo(CHARGE);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);
    }

    @Test
    public void whenTransactionTypeIsSetInData_createTransactionDraftBuilder_usesValueFromData() {
        transactionData.setTransactionType(CHARGE);
        TransactionDraft transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, AUTHORIZATION).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getType()).isEqualByComparingTo(CHARGE);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);

        transactionData.setTransactionType(AUTHORIZATION);
        transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, CHARGE).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getType()).isEqualByComparingTo(AUTHORIZATION);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);
    }

    @Test
    public void whenTransactionTypeSkippedInData_createTransactionDraftBuilder_usesValueFromDefault() {
        TransactionDraft transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, AUTHORIZATION).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getType()).isEqualByComparingTo(AUTHORIZATION);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);

        transactionDraft = createPaymentTransactionMethod.createTransactionDraftBuilder(transactionData, CHARGE).build();
        assertThat(transactionDraft).isNotNull();
        assertThat(transactionDraft.getType()).isEqualByComparingTo(CHARGE);
        assertThat(transactionDraft.getState()).isEqualByComparingTo(INITIAL);
    }
}