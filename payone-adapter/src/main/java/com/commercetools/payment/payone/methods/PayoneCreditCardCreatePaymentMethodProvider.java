package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFields;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFieldsIfExist;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class PayoneCreditCardCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    private PayoneCreditCardCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PayonePaymentMethodType.PAYMENT_CREDIT_CARD.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneCreditCardCreatePaymentMethodProvider();
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {
        CustomFieldsDraftBuilder customFieldsDraftBuilder = createCustomFieldsBuilder(cpd);

        mapDraftCustomFields(customFieldsDraftBuilder, cpd, asList(
                SUCCESS_URL,
                ERROR_URL,
                CANCEL_URL,
                CREDIT_CARD_CARD_DATA_PLACEHOLDER,
                CREDIT_CARD_TRUNCATED_CARD_NUMBER));

        mapDraftCustomFields(customFieldsDraftBuilder, cpd, singletonList(CREDIT_CARD_FORCE_3D_SECURE), Boolean::valueOf);

        mapDraftCustomFieldsIfExist(customFieldsDraftBuilder, cpd, asList(
                CREDIT_CARD_CARD_NETWORK,
                CREDIT_CARD_EXPIRY_DATE,
                CREDIT_CARD_CARD_HOLDER_NAME));

        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
