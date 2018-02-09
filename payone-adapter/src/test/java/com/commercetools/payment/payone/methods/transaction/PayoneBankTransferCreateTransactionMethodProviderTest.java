package com.commercetools.payment.payone.methods.transaction;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.BANK_TRANSFER_ADVANCE;
import static io.sphere.sdk.payments.TransactionType.AUTHORIZATION;

@RunWith(MockitoJUnitRunner.class)
public class PayoneBankTransferCreateTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        applyCustomFieldStubbing();
        transactionMethod = PayoneBankTransferCreateTransactionMethodProvider.of();
    }

    @Test
    public void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled() {
        PaymentMethodInfo paymentInterface = PaymentMethodInfoBuilder.of()
                .paymentInterface("mockPaymentInterface")
                .method(BANK_TRANSFER_ADVANCE).build();
        applyPaymentStubbing(paymentInterface, Money.of(12.34, "EUR"));

        super.whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(AUTHORIZATION, Money.of(12.34, "EUR"));
    }

    @Test
    @Override
    public void handleSuccessfulServiceCall_withRedirectUrl() throws Exception {
        super.handleSuccessfulServiceCall_withRedirectUrl();
    }

    @Test
    @Override
    public void handleSuccessfulServiceCall_withoutRedirectUrl() throws Exception {
        super.handleSuccessfulServiceCall_withoutRedirectUrl();
    }
}