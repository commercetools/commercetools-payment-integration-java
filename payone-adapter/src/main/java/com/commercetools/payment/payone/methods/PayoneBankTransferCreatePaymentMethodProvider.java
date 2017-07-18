package com.commercetools.payment.payone.methods;

import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_BANK_TRANSFER;

public class PayoneBankTransferCreatePaymentMethodProvider extends PayoneRedirectPaymentMethodBase {
    private PayoneBankTransferCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_BANK_TRANSFER.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneBankTransferCreatePaymentMethodProvider();
    }

}
