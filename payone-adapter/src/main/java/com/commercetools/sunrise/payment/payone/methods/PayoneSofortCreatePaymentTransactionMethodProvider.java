package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayoneSofortCreatePaymentTransactionMethodProvider extends CreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {
    public static CreatePaymentTransactionMethod of() {
        return new PayoneSofortCreatePaymentTransactionMethodProvider();
    }

    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> create() {
        return null;
    }
}
