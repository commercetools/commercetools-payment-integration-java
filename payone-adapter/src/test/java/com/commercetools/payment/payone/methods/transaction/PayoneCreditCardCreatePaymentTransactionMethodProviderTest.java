package com.commercetools.payment.payone.methods.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PayoneCreditCardCreatePaymentTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        applyStubbing();
        transactionMethod = PayoneCreditCardCreatePaymentTransactionMethodProvider.of();
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