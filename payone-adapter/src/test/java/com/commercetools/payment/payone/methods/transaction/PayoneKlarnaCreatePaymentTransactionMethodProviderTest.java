package com.commercetools.payment.payone.methods.transaction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PayoneKlarnaCreatePaymentTransactionMethodProviderTest
        extends BasePayoneCreatePaymentTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        transactionMethod = PayoneKlarnaCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    public void handleSuccessfulServiceCall() throws Exception {
        handleSuccessfulServiceCall_default();
    }
}