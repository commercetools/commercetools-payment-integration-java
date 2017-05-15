package com.commercetools.payment.payone.methods.transaction;

public class PayoneBankTransferCreateTransactionMethodProvider extends PayoneRedirectCreatePaymentTransactionMethodProviderBase {

    private PayoneBankTransferCreateTransactionMethodProvider() {
    }

    public static PayoneBankTransferCreateTransactionMethodProvider of() {
        return new PayoneBankTransferCreateTransactionMethodProvider();
    }
}
