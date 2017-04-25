package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

/**
 * @author mht@dotsource.de
 */
public class PayoneBankTransferCreateTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    private PayoneBankTransferCreateTransactionMethodProvider() {
    }

    public static CreatePaymentTransactionMethod of() {
        return new PayoneBankTransferCreateTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        return redirectSuccessfulServiceCall(updatedPayment);
    }
}
