package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.payone.PayoneHelper;
import com.commercetools.sunrise.payment.utils.PaymentDraftHelper;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by mgatz on 7/21/16.
 */
public class CreatePayoneCreditCardMethod {

    private static final String THIS_PAYMENT_METHOD = "CREDIT_CARD";

    private CreatePayoneCreditCardMethod() {
    }

    public static Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
            // check payment without transaction for this method exists
            PaymentLookupHelper.of(cpd.getSphereClient())
                    .findPaymentWithoutTransaction(cpd.getCart(), PayoneHelper.getPaymentServiceProviderId(), THIS_PAYMENT_METHOD)
                    .thenCompose(payment -> {
                        // create payment at CTP
                        PaymentDraft draft = createPaymentDraft(cpd);
                        return cpd.getSphereClient().execute(PaymentCreateCommand.of(draft));
                    })
                    .thenApply(payment -> {
                        if(null == payment) {
                            return PaymentCreationResultBuilder
                                    .of(OperationResult.FAILED)
                                    .handlingTask(HandlingTask.of(ShopAction.HANDLE_ERROR))
                                    .build();
                        }

                        return PaymentCreationResultBuilder
                                .of(OperationResult.SUCCESS)
                                .payment(payment)
                                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                                .build();
                    });
    }

    private static PaymentDraft createPaymentDraft(CreatePaymentData cpd) {
        return PaymentDraftHelper.createPaymentDraftBuilder(cpd)
                .paymentMethodInfo(PayoneHelper.getPayonePropertiesLoadingHelper().getMethodInfo(THIS_PAYMENT_METHOD))
                .build();
    }
}
