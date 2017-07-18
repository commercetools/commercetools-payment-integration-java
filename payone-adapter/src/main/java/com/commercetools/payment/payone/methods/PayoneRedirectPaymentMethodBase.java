package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;

/**
 * Base class for payment methods with redirect transaction action (Paypal, Sofortüberweisung etc).
 */
public abstract class PayoneRedirectPaymentMethodBase extends PayoneCreatePaymentMethodBase {

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {
        return super.createPaymentDraft(cpd)
                    .custom(createCustomFieldsBuilder(cpd)
                        .addObject(SUCCESS_URL, cpd.getConfigByName(SUCCESS_URL))
                        .addObject(ERROR_URL, cpd.getConfigByName(ERROR_URL))
                        .addObject(CANCEL_URL, cpd.getConfigByName((CANCEL_URL)))
                        .build());
    }
}
