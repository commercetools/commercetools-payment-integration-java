package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

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
        return PaymentTransactionCreationResultBuilder.ofError(
                "There was no redirect set at the payment object which is required for Paypal ("
                + updatedPayment.getId()
                +"). Check Payone Connector log files!");
    }
}
