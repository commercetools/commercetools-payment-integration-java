package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.methods.CreatePaymentTransactionMethod;

public class PayonePaypalCreatePaymentTransactionMethodProvider extends PayoneRedirectCreatePaymentTransactionMethodProviderBase {

    public static CreatePaymentTransactionMethod of() {
        return new PayonePaypalCreatePaymentTransactionMethodProvider();
    }
}
