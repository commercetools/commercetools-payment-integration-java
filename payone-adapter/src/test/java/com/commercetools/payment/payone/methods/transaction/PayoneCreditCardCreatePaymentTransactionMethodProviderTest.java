package com.commercetools.payment.payone.methods.transaction;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.CREDIT_CARD;
import static io.sphere.sdk.payments.TransactionType.CHARGE;

@RunWith(MockitoJUnitRunner.class)
public class PayoneCreditCardCreatePaymentTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        applyCustomFieldStubbing();
        transactionMethod = PayoneCreditCardCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    public void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled() {
        PaymentMethodInfo paymentInterface = PaymentMethodInfoBuilder.of()
                .paymentInterface("mockPaymentInterface")
                .method(CREDIT_CARD).build();
        applyPaymentStubbing(paymentInterface, Money.of(67.99, "UAH"));

        super.whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(CHARGE, Money.of(67.99, "UAH"));
    }

    @Test
    @Override
    public void handleSuccessfulServiceCall_withRedirectUrl() throws Exception {
        super.handleSuccessfulServiceCall_withRedirectUrl();
    }

    /**
     * Opposite to other redirect-bases payment methods, Credit card might have payments without redirect
     * (without 3DS verification).
     */
    @Test
    public void handleSuccessfulServiceCall_success_withoutRedirectUrl() throws Exception {
        super.handleSuccessfulServiceCall_default();
    }

}