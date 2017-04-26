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

import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_CASH_ADVANCE;

/**
 *
 * @author mht@dotsource.de
 *
 */
public class PayoneBanktransferInAdvanceCreatePaymentProvider extends PayoneCreatePaymentMethodBase {
    private PayoneBanktransferInAdvanceCreatePaymentProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_CASH_ADVANCE.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneBanktransferInAdvanceCreatePaymentProvider();
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
                .custom(createCustomFieldsBuilder(cpd).build());
    }
}
