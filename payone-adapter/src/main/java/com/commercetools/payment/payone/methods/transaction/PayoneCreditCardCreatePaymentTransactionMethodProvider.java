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
 * Created by mgatz on 7/27/16.
 */
public class PayoneCreditCardCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    public static CreatePaymentTransactionMethod of() {
        return new PayoneCreditCardCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        // check for a redirect URL as this leads us to 3D secure
        String redirectURL = getCustomFieldStringIfExists(updatedPayment, REDIRECT_URL);
        if (null != redirectURL) {
            return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                    .payment(updatedPayment)
                    .handlingTask(HandlingTask.of(ShopAction.REDIRECT).redirectUrl(redirectURL)).build();
        }

        // no redirect URL
        // we'll assume that this is perfectly fine for now
        return PaymentTransactionCreationResultBuilder
                .of(OperationResult.SUCCESS)
                .payment(updatedPayment)
                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                .build();
    }
}
