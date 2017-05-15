package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_INVOICE_KLARNA;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFields;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFieldsIfExist;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class PayoneKlarnaCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    private PayoneKlarnaCreatePaymentMethodProvider() {
    }

    @Override
    protected String getMethodType() {
        return PAYMENT_INVOICE_KLARNA.getValue();
    }

    public static PayoneKlarnaCreatePaymentMethodProvider of() {
        return new PayoneKlarnaCreatePaymentMethodProvider();
    }

    @Override
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        CustomFieldsDraftBuilder customFieldsDraftBuilder = createCustomFieldsBuilder(cpd);
        mapDraftCustomFields(customFieldsDraftBuilder, cpd, asList(GENDER, IP, TELEPHONE_NUMBER, BIRTHDAY));
        mapDraftCustomFieldsIfExist(customFieldsDraftBuilder, cpd, singletonList(NARRATIVE_TEXT));

        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
