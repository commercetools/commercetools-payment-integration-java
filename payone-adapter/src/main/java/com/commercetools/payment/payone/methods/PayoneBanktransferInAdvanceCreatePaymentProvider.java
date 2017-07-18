package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;

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
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {
        return super.createPaymentDraft(cpd)
                .custom(createCustomFieldsBuilder(cpd).build());
    }
}
