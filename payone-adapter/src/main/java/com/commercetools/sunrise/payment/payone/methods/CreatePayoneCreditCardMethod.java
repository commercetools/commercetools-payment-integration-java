package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.payone.PayoneHelper;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by mgatz on 7/21/16.
 */
public class CreatePayoneCreditCardMethod {

    private final CreatePaymentData createPaymentData;

    private CreatePayoneCreditCardMethod(CreatePaymentData cpd) {
        this.createPaymentData = cpd;
    }

    public static Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {

        return cpd ->
            // check payment without transaction for this method exists
            PaymentLookupHelper.of(cpd.getSphereClient())
                    .findPaymentWithoutTransaction(cpd.getCart(), PayoneHelper.getPaymentServiceProviderId(), "CREDIT_CARD")
                    .thenApply(payment -> {
                        // create payment at CTP

                        // create task object

                        // create return object by builder
                        return PaymentCreationResultBuilder.of(OperationResult.SUCCESS).build();
                    })
                    .exceptionally(e -> {
                        return PaymentCreationResultBuilder.of(OperationResult.FAILED).build();
                    });
    }
}
