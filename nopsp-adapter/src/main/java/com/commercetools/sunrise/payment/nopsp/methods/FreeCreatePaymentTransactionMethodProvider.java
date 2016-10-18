package com.commercetools.sunrise.payment.nopsp.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.utils.CompletableFutureUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * @author mht@dotsource.de
 */
public class FreeCreatePaymentTransactionMethodProvider implements CreatePaymentTransactionMethod {
    private FreeCreatePaymentTransactionMethodProvider() {
    }

    public static FreeCreatePaymentTransactionMethodProvider of() {
        return new FreeCreatePaymentTransactionMethodProvider();
    }

    /**
     * We dont create an actual Transaction on CTP
     * @return the <em>empty</em> PaymentTransactionCreationResult
     */
    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> create() {
        return cptd -> CompletableFuture.completedFuture(
                PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                        .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                        .build());
    }
}
