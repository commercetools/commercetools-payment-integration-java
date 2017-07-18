package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

public class PayoneCreditCardCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    public static PayoneCreditCardCreatePaymentTransactionMethodProvider of() {
        return new PayoneCreditCardCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        // check for a redirect URL as this leads us to 3D secure,
        // otherwise fallback to default "continue" action,
        // because this is normal behavior for cards without 3DS
        return handleRedirectIfPresent(updatedPayment, this::defaultSuccessTransactionCreationResult);
    }

}
