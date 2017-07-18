package com.commercetools.payment.nopsp.methods;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.payment.methods.CreatePaymentMethod;
import com.commercetools.payment.methods.CreatePaymentMethodBase;
import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.PaymentCreationResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * @author mht@dotsource.de
 */
public class FreeCreatePaymentMethodProvider extends CreatePaymentMethodBase implements CreatePaymentMethod {
    private FreeCreatePaymentMethodProvider() {
    }

    public static FreeCreatePaymentMethodProvider of() {
        return new FreeCreatePaymentMethodProvider();
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                addNewPayment(cpd)
                        .thenApply(payment -> PaymentCreationResultBuilder
                                .of(OperationResult.SUCCESS)
                                .payment(payment)
                                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                                .build())
                        .exceptionally(ex -> PaymentCreationResultBuilder
                                .ofError("An error occurred during creation of [nopsp] payment object", ex));
    }

}
