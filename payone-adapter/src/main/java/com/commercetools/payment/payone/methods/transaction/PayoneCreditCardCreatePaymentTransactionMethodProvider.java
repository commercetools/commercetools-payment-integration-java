package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

public class PayoneCreditCardCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    public static CreatePaymentTransactionMethod of() {
        return new PayoneCreditCardCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        // check for a redirect URL as this leads us to 3D secure
        return handleRedirectIfPresent(updatedPayment, this::noRedirectCreditCardFallback);
    }

    /**
     * No redirect URL is ok for credit cards without verification.
     * @param paymentWithoutRedirect updated payment reference
     * @return {@link PaymentTransactionCreationResult} with {@link OperationResult#SUCCESS}
     * and {@link ShopAction#CONTINUE}.
     */
    private PaymentTransactionCreationResult noRedirectCreditCardFallback(Payment paymentWithoutRedirect) {
        return PaymentTransactionCreationResultBuilder
                .of(OperationResult.SUCCESS)
                .payment(paymentWithoutRedirect)
                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                .build();
    }
}
