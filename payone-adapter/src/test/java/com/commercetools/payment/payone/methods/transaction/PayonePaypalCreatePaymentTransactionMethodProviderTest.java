package com.commercetools.payment.payone.methods.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PayonePaypalCreatePaymentTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        applyStubbing();
        transactionMethod = PayonePaypalCreatePaymentTransactionMethodProvider.of();
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