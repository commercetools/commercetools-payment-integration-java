package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;

import static  com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.PAID_TO_IBAN;
import static  com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.PAID_TO_BIC;
import static  com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.PAID_TO_NAME;

import io.sphere.sdk.payments.Payment;

public class PayoneBanktransferInAdvancePaymentTransactionMethodProvider
        extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    private PayoneBanktransferInAdvancePaymentTransactionMethodProvider() {
    }

    public static CreatePaymentTransactionMethod of() {
        return new PayoneBanktransferInAdvancePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        final String payToIBAN = updatedPayment.getCustom().getFieldAsString(PAID_TO_IBAN);
        final String payToBIC = updatedPayment.getCustom().getFieldAsString(PAID_TO_BIC);
        final String payToName = updatedPayment.getCustom().getFieldAsString(PAID_TO_NAME);
        if (null != payToIBAN && null != payToBIC && null != payToName) {
            return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS).payment(updatedPayment)
                    .handlingTask(HandlingTask.of(ShopAction.CONTINUE)
                            .addData(PAID_TO_IBAN, payToIBAN)
                            .addData(PAID_TO_BIC, payToBIC)
                            .addData(PAID_TO_NAME, payToName))
                    .build();
        }
        return PaymentTransactionCreationResultBuilder
                .ofError("There was no bank account data set at the payment object which is required for prepaiment ("
                        + updatedPayment.getId() + "). Check Payone Connector log files!");
    }
}
