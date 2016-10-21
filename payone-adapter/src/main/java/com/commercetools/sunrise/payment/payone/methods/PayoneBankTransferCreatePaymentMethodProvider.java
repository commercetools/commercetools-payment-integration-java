package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentDraftBuilder;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_BANK_TRANSFER;

/**
 * @author mht@dotsource.de
 * Generic Implementation for all <em>payment-BANK_TRANSFER</em> types that just uses redirect.
 */
public class PayoneBankTransferCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {
    private PayoneBankTransferCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_BANK_TRANSFER.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneBankTransferCreatePaymentMethodProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                removePaymentsAndCreateNew(cpd)
                        .thenApply(payment -> null != payment
                                ? PaymentCreationResultBuilder
                                .of(OperationResult.SUCCESS)
                                .payment(payment)
                                .handlingTask(HandlingTask.of(ShopAction.REDIRECT))
                                .build()
                                : PaymentCreationResultBuilder.ofError("An error occured during creation of the payment object."));
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {
        return super.createPaymentDraft(cpd)
                .custom(createCustomFieldsBuilder(cpd)
                        .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                        .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                        .addObject(CANCEL_URL, cpd.getConfigByName((CANCEL_URL)))
                        .build());
    }
}
