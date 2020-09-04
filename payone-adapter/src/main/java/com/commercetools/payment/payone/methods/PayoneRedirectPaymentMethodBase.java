package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;
import org.apache.commons.lang3.StringUtils;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.BANK_GROUP_TYPE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CANCEL_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.ERROR_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.SUCCESS_URL;

/**
 * Base class for payment methods with redirect transaction action (Paypal, Sofortüberweisung etc).
 */
public abstract class PayoneRedirectPaymentMethodBase extends PayoneCreatePaymentMethodBase {

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {
        CustomFieldsDraftBuilder customFieldBuilder = createCustomFieldsBuilder(cpd)
                .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                .addObject(CANCEL_URL, cpd.getConfigByName(CANCEL_URL));
        String bankGroupType = cpd.getConfigByName(BANK_GROUP_TYPE);
        if (StringUtils.isNotBlank(bankGroupType)) {
            customFieldBuilder = customFieldBuilder.addObject(BANK_GROUP_TYPE, bankGroupType);
        }
        return super.createPaymentDraft(cpd).custom(customFieldBuilder.build());
    }
}
