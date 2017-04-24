package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentDraftBuilder;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_CASH_ADVANCE;

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
