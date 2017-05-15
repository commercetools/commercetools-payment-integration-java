package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REDIRECT_URL;

/**
 * Base class for redirect transactions, like Paypal or Sofortüberweisung.
 */
public abstract class PayoneRedirectCreatePaymentTransactionMethodProviderBase extends PayoneCreatePaymentTransactionMethodBase
        implements CreatePaymentTransactionMethod {

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        String redirectURL = getCustomFieldStringIfExists(updatedPayment, REDIRECT_URL);
        if (null != redirectURL) {
            return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                    .payment(updatedPayment)
                    .handlingTask(HandlingTask.of(ShopAction.REDIRECT).redirectUrl(redirectURL)).build();
        }
        String errorMessage = "Payment provider redirect URL is not available.";
        return handleError(errorMessage, updatedPayment);
    }
}
