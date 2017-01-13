package com.commercetools.sunrise.payment.payone.methods.transaction;

import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayonePaypalCreatePaymentTransactionMethodProvider
        extends PayoneCreatePaymentTransactionMethodBase
        implements CreatePaymentTransactionMethod {

    public static CreatePaymentTransactionMethod of() {
        return new PayonePaypalCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        return redirectSuccessfulServiceCall(updatedPayment);
    }
}
