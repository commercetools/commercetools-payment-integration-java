package com.commercetools.sunrise.payment.methods;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by mgatz on 7/26/16.
 */
public interface CreatePaymentMethod {
    /**
     * Provides a function that handles the creation of a payment object within the CTP plattform.
     * @return an executable function
     */
    Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create();
}
