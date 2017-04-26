package com.commercetools.payment.payone.methods;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentDraftBuilder;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_WALLET;

/**
 * Created by mgatz on 7/26/16.
 */
public class PayonePaypalCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    private PayonePaypalCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_WALLET.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayonePaypalCreatePaymentMethodProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                addNewPayment(cpd)
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


