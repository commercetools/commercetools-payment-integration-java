package com.commercetools.payment.payone.methods;

import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_WALLET;

public class PayoneWalletCreatePaymentMethodProvider extends PayoneRedirectPaymentMethodBase {

    private PayoneWalletCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_WALLET.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneWalletCreatePaymentMethodProvider();
    }

}


