package com.commercetools.sunrise.payment.payone.methods.transaction;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentStatus;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.REDIRECT_URL;

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
        String redirectURL = updatedPayment.getCustom().getFieldAsString(REDIRECT_URL);
        if (null != redirectURL) {
            return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                    .payment(updatedPayment)
                    .handlingTask(HandlingTask.of(ShopAction.REDIRECT).redirectUrl(redirectURL)).build();
        }
        String errorMessage = "Payment provider redirect URL is not available.";
        if (updatedPayment.getPaymentStatus() != null) {
            PaymentStatus paymentStatus = updatedPayment.getPaymentStatus();
            errorMessage = errorMessage + String.format("Error code: %s, Error message: %s", paymentStatus.getInterfaceCode(), paymentStatus.getInterfaceText());
        }
        return PaymentTransactionCreationResultBuilder.ofError(errorMessage, null, updatedPayment);
    }
}
