package com.commercetools.payment.payone.methods.transaction;

import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.types.CustomFields;
import org.mockito.Mock;

/**
 * Inherited classes must be run with {@code @RunWith(MockitoJUnitRunner.class)}
 */
public class BasePayoneCreatePaymentTransactionMethodProviderTest {

    protected PayoneCreatePaymentTransactionMethodBase transactionMethod;

    @Mock
    protected Payment payment;

    @Mock
    protected CustomFields customFields;
}
