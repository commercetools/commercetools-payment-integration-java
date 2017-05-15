package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

public class PayoneKlarnaCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase {

    public static PayoneKlarnaCreatePaymentTransactionMethodProvider of() {
        return new PayoneKlarnaCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        return defaultSuccessTransactionCreationResult(updatedPayment);
    }
}
