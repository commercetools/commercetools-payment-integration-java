package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_CREDIT_CARD;

/**
 * Created by mgatz on 7/21/16.
 */
public class PayoneCreditCardCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    private PayoneCreditCardCreatePaymentMethodProvider() { }

    @Override
    protected String getMethodType() {
        return PAYMENT_CREDIT_CARD.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneCreditCardCreatePaymentMethodProvider();
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

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        CustomFieldsDraftBuilder customFieldsDraftBuilder = createCustomFieldsBuilder(cpd)
                .addObject(CREDIT_CARD_FORCE_3D_SECURE, Boolean.valueOf(cpd.getConfigByName(CREDIT_CARD_FORCE_3D_SECURE)))
                .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                .addObject(CANCEL_URL, cpd.getConfigByName(CANCEL_URL))
                .addObject(CREDIT_CARD_CARD_DATA_PLACEHOLDER, cpd.getConfigByName(CREDIT_CARD_CARD_DATA_PLACEHOLDER))
                .addObject(CREDIT_CARD_TRUNCATED_CARD_NUMBER, cpd.getConfigByName(CREDIT_CARD_TRUNCATED_CARD_NUMBER));

        if(StringUtils.isNotEmpty(cpd.getConfigByName(CREDIT_CARD_CARD_NETWORK))) {
            customFieldsDraftBuilder.addObject(CREDIT_CARD_CARD_NETWORK, cpd.getConfigByName(CREDIT_CARD_CARD_NETWORK));
        }
        if(StringUtils.isNotEmpty(cpd.getConfigByName(CREDIT_CARD_EXPIRY_DATE))) {
            customFieldsDraftBuilder.addObject(CREDIT_CARD_EXPIRY_DATE, cpd.getConfigByName(CREDIT_CARD_EXPIRY_DATE));
        }
        if(StringUtils.isNotEmpty(cpd.getConfigByName(CREDIT_CARD_CARD_HOLDER_NAME))) {
            customFieldsDraftBuilder.addObject(CREDIT_CARD_CARD_HOLDER_NAME, cpd.getConfigByName(CREDIT_CARD_CARD_HOLDER_NAME));
        }

        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
