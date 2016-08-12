package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.commands.updateactions.SetCustomField;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;

/**
 * Created by mgatz on 7/21/16.
 */
public class PayoneCreditCardCreatePaymentMethodProvider extends CreatePaymentMethodBase implements CreatePaymentMethod {

    private PayoneCreditCardCreatePaymentMethodProvider() { }

    public static CreatePaymentMethodBase of() {
        return new PayoneCreditCardCreatePaymentMethodProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                removePaymentsAndCreateNew(cpd)
                .thenApply(payment -> null != payment
                        ? PaymentCreationResultBuilder
                            .of(OperationResult.SUCCESS)
                            .payment(payment)
                            .handlingTask(HandlingTask.of(ShopAction.REQUIRE_INPUT))
                            .build()
                        : PaymentCreationResultBuilder.ofError("An error occured during creation of the payment object."));
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        return super.createPaymentDraft(cpd)
                .custom(CustomFieldsDraftBuilder.ofTypeKey("payment-CREDIT_CARD")
                        .addObject("reference", cpd.getReference())
                        .addObject("languageCode", getLanguageFromCartOrFallback(cpd.getCart()))
                        .addObject(CREDIT_CARD_FORCE_3D_SECURE, Boolean.valueOf(cpd.getConfigByName(CREDIT_CARD_FORCE_3D_SECURE)))
                        .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                        .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                        .addObject(CANCEL_URL, cpd.getConfigByName((CANCEL_URL)))
                        .build());
    }
}
