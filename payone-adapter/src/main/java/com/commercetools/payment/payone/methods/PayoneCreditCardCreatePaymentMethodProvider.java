package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.payone.config.PayoneConfigurationNames;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;
import org.apache.commons.lang3.StringUtils;

public class PayoneCreditCardCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    private PayoneCreditCardCreatePaymentMethodProvider() { }

    @Override
    protected String getMethodType() {
        return PayonePaymentMethodType.PAYMENT_CREDIT_CARD.getValue();
    }

    public static PayoneCreatePaymentMethodBase of() {
        return new PayoneCreditCardCreatePaymentMethodProvider();
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        CustomFieldsDraftBuilder customFieldsDraftBuilder = createCustomFieldsBuilder(cpd)
                .addObject(PayoneConfigurationNames.CREDIT_CARD_FORCE_3D_SECURE, Boolean.valueOf(cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_FORCE_3D_SECURE)))
                .addObject(PayoneConfigurationNames.SUCCESS_URL, cpd.getConfigByName(PayoneConfigurationNames.SUCCESS_URL))
                .addObject(PayoneConfigurationNames.ERROR_URL, cpd.getConfigByName(PayoneConfigurationNames.ERROR_URL))
                .addObject(PayoneConfigurationNames.CANCEL_URL, cpd.getConfigByName(PayoneConfigurationNames.CANCEL_URL))
                .addObject(PayoneConfigurationNames.CREDIT_CARD_CARD_DATA_PLACEHOLDER, cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_CARD_DATA_PLACEHOLDER))
                .addObject(PayoneConfigurationNames.CREDIT_CARD_TRUNCATED_CARD_NUMBER, cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_TRUNCATED_CARD_NUMBER));

        if (StringUtils.isNotEmpty(cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_CARD_NETWORK))) {
            customFieldsDraftBuilder.addObject(PayoneConfigurationNames.CREDIT_CARD_CARD_NETWORK, cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_CARD_NETWORK));
        }
        if (StringUtils.isNotEmpty(cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_EXPIRY_DATE))) {
            customFieldsDraftBuilder.addObject(PayoneConfigurationNames.CREDIT_CARD_EXPIRY_DATE, cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_EXPIRY_DATE));
        }
        if (StringUtils.isNotEmpty(cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_CARD_HOLDER_NAME))) {
            customFieldsDraftBuilder.addObject(PayoneConfigurationNames.CREDIT_CARD_CARD_HOLDER_NAME, cpd.getConfigByName(PayoneConfigurationNames.CREDIT_CARD_CARD_HOLDER_NAME));
        }

        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
