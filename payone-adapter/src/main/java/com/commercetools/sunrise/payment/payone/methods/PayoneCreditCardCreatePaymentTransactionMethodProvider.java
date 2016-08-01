package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.commands.updateactions.SetCustomField;

import java.util.List;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayoneCreditCardCreatePaymentTransactionMethodProvider extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    public static CreatePaymentTransactionMethod of() {
        return new PayoneCreditCardCreatePaymentTransactionMethodProvider();
    }

    @Override
    protected List<UpdateAction<Payment>> addPaymentUpdateActions(CreatePaymentTransactionData data) {

        List<UpdateAction<Payment>> updateActions = super.addPaymentUpdateActions(data);

        // add credit card required fields
        updateActions.add(SetCustomField.ofObject(CREDIT_CARD_CARD_DATA_PLACEHOLDER, data.getConfigByName(CREDIT_CARD_CARD_DATA_PLACEHOLDER)));
        updateActions.add(SetCustomField.ofObject(CREDIT_CARD_MASKED_CARD_NUMBER, data.getConfigByName(CREDIT_CARD_MASKED_CARD_NUMBER)));
        updateActions.add(SetCustomField.ofObject(CREDIT_CARD_CARD_HOLDER_NAME, data.getConfigByName(CREDIT_CARD_CARD_HOLDER_NAME)));
        updateActions.add(SetCustomField.ofObject(CREDIT_CARD_EXPIRY_DATE, data.getConfigByName(CREDIT_CARD_EXPIRY_DATE)));
        updateActions.add(SetCustomField.ofObject(CREDIT_CARD_CARD_NETWORK, data.getConfigByName(CREDIT_CARD_CARD_NETWORK)));

        return updateActions;
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        if(updatedPayment.getCustom().getFieldAsBoolean(CREDIT_CARD_FORCE_3D_SECURE)) {
            // check for a redirect URL as this leads us to 3D secure
            String redirectURL = updatedPayment.getCustom().getFieldAsString(REDIRECT_URL);
            if (null != redirectURL) {
                return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                        .payment(updatedPayment)
                        .handlingTask(HandlingTask.of(ShopAction.REDIRECT).redirectUrl(redirectURL)).build();
            }
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
