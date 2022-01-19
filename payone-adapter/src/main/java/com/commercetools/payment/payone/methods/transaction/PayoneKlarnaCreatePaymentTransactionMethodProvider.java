package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

public class PayoneKlarnaCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase {

    public static PayoneKlarnaCreatePaymentTransactionMethodProvider of() {
        return new PayoneKlarnaCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        return handleRedirectIfPresent(updatedPayment, this::noRedirectFallback);
    }
    /**
     * For most of "redirect-based" method this action is mandatory, thus if redirect URL not found - error is returned.
     * @param paymentWithoutRedirect updated payment reference
     * @return {@link PaymentTransactionCreationResult} with {@link OperationResult#FAILED}
     * and {@link ShopAction#HANDLE_ERROR}.
     */
    protected PaymentTransactionCreationResult noRedirectFallback(Payment paymentWithoutRedirect) {
        return handleError("Redirect URL provided from Klarna is not available.", paymentWithoutRedirect);
    }
}
