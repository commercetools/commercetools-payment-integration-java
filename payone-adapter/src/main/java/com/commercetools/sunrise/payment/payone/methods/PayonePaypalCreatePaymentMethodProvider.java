package com.commercetools.sunrise.payment.payone.methods;

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
 * Created by mgatz on 7/26/16.
 */
public class PayonePaypalCreatePaymentMethodProvider extends CreatePaymentMethodBase implements CreatePaymentMethod {

    private PayonePaypalCreatePaymentMethodProvider() {
    }

    public static CreatePaymentMethodBase of() {
        return new PayonePaypalCreatePaymentMethodProvider();
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
                        : PaymentCreationResultBuilder.ofError());
    }
}
