package com.commercetools.payment.payone.methods.transaction;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.WALLET_PAYPAL;
import static io.sphere.sdk.payments.TransactionType.CHARGE;

@RunWith(MockitoJUnitRunner.class)
public class PayonePaypalCreatePaymentTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        applyCustomFieldStubbing();
        transactionMethod = PayonePaypalCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    public void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled() {
        PaymentMethodInfo paymentInterface = PaymentMethodInfoBuilder.of()
                .paymentInterface("mockPaymentInterface")
                .method(WALLET_PAYPAL).build();
        applyPaymentStubbing(paymentInterface, Money.of(11.22, "ZWD"));

        super.whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(CHARGE, Money.of(11.22, "ZWD"));
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