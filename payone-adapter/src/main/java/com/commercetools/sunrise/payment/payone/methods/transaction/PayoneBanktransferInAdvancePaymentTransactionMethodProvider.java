package com.commercetools.sunrise.payment.payone.methods.transaction;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;

public class PayoneBanktransferInAdvancePaymentTransactionMethodProvider
        extends PayoneCreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    private PayoneBanktransferInAdvancePaymentTransactionMethodProvider() {
    }

    public static CreatePaymentTransactionMethod of() {
        return new PayoneBanktransferInAdvancePaymentTransactionMethodProvider();
    }

    @Override
    protected PaymentTransactionCreationResult handleSuccessfulServiceCall(Payment updatedPayment) {
        final String payToIBAN =  updatedPayment.getCustom() != null ?updatedPayment.getCustom().getFieldAsString(PAID_TO_IBAN):null;
        final String payToBIC =  updatedPayment.getCustom() != null ?updatedPayment.getCustom().getFieldAsString(PAID_TO_BIC):null;
        final String payToName = updatedPayment.getCustom() != null ? updatedPayment.getCustom().getFieldAsString(PAID_TO_NAME):null;
        if (null != payToIBAN && null != payToBIC && null != payToName) {
            return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS).payment(updatedPayment)
                    .handlingTask(HandlingTask.of(ShopAction.CONTINUE)
                            .addData(PAID_TO_IBAN, payToIBAN)
                            .addData(PAID_TO_BIC, payToBIC)
                            .addData(PAID_TO_NAME, payToName))
                    .build();
        }
        String errorMessage = "No bank account data available.";
        return handleError(errorMessage, updatedPayment);

    }
}
