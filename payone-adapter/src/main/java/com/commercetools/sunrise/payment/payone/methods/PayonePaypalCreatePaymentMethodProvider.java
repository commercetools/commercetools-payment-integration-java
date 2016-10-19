package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentDraftBuilder;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_WALLET;

/**
 * Created by mgatz on 7/26/16.
 */
public class PayonePaypalCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase implements CreatePaymentMethod {

    private PayonePaypalCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_WALLET.getValue();
    }

    public static CreatePaymentMethodBase of() {
        return new PayonePaypalCreatePaymentMethodProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                removePaymentsAndCreateNew(cpd)
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
                .custom(createCustomFieldsBuilder(cpd)
                        .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                        .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                        .addObject(CANCEL_URL, cpd.getConfigByName((CANCEL_URL)))
                    .build());
    }
}


