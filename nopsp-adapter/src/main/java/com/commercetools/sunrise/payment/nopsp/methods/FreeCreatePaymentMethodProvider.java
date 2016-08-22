package com.commercetools.sunrise.payment.nopsp.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;

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
                removePaymentsAndCreateNew(cpd)
                        .thenApply(payment -> null != payment
                                ? PaymentCreationResultBuilder
                                .of(OperationResult.SUCCESS)
                                .payment(payment)
                                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                                .build()
                                : PaymentCreationResultBuilder.ofError("An error occured during creation of the payment object."));
    }

}