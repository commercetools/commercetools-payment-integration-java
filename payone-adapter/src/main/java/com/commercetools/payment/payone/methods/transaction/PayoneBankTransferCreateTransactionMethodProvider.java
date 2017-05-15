package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.methods.CreatePaymentTransactionMethod;

public class PayoneBankTransferCreateTransactionMethodProvider extends PayoneRedirectCreatePaymentTransactionMethodProviderBase {

    private PayoneBankTransferCreateTransactionMethodProvider() {
    }

    public static CreatePaymentTransactionMethod of() {
        return new PayoneBankTransferCreateTransactionMethodProvider();
    }
}
