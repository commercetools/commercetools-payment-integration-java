package com.commercetools.payment.payone.methods.transaction;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.INVOICE_KLARNA;
import static io.sphere.sdk.payments.TransactionType.CHARGE;

@RunWith(MockitoJUnitRunner.class)
public class PayoneKlarnaCreatePaymentTransactionMethodProviderTest
        extends BasePayoneCreatePaymentTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        transactionMethod = PayoneKlarnaCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    public void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled() {
        PaymentMethodInfo paymentInterface = PaymentMethodInfoBuilder.of()
                .paymentInterface("mockPaymentInterface")
                .method(INVOICE_KLARNA)
                .build();
        applyPaymentStubbing(paymentInterface, Money.of(666.66, "INR"));

        super.whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(CHARGE, Money.of(666.66, "INR"));
    }

    @Test
    public void handleSuccessfulServiceCall() throws Exception {
        handleSuccessfulServiceCall_default();
    }
}