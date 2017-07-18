package com.commercetools.payment.payone.methods;

import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_WALLET;

public class PayonePaypalCreatePaymentMethodProvider extends PayoneRedirectPaymentMethodBase {

    private PayonePaypalCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_WALLET.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayonePaypalCreatePaymentMethodProvider();
    }

}


