package com.commercetools.sunrise.payment.methods;

import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by mgatz on 7/27/16.
 */
public interface CreatePaymentTransactionMethod {
    /**
     * Provides a function that handles the creation of a payment transaction object within the CTP plattform.
     * @return an executable function
     */
    Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> create();
}
