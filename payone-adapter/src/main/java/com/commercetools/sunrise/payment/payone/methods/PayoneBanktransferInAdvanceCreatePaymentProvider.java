package com.commercetools.sunrise.payment.payone.methods;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;

import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

/**
 * 
 * @author mht@dotsource.de
 *
 */
public class PayoneBanktransferInAdvanceCreatePaymentProvider extends CreatePaymentMethodBase implements CreatePaymentMethod {
    private PayoneBanktransferInAdvanceCreatePaymentProvider() {
    }

    public static CreatePaymentMethodBase of() {
        return new PayoneBanktransferInAdvanceCreatePaymentProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
            createOrUpdatePayment(cpd)
                .thenApply(payment -> null != payment
                        ? PaymentCreationResultBuilder
                            .of(OperationResult.SUCCESS)
                            .payment(payment)
                            .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                            .build()
                        : PaymentCreationResultBuilder.ofError("An error occured during creation of the payment object."));
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        return super.createPaymentDraft(cpd)
                .custom(CustomFieldsDraftBuilder.ofTypeKey("payment-BANK_TRANSFER")
                        .addObject("reference", cpd.getReference())
                        .addObject("languageCode", getLanguageFromCartOrFallback(cpd.getCart()))
                    .build());
    }
}
