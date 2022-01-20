package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.BIRTHDAY;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CANCEL_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.ERROR_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.GENDER;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.IP;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.SUCCESS_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.TELEPHONENUMBER;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_INVOICE_KLARNA;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFieldsIfExist;
import static java.util.Arrays.asList;

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
        mapDraftCustomFieldsIfExist(customFieldsDraftBuilder, cpd, asList(SUCCESS_URL, ERROR_URL, CANCEL_URL, GENDER, IP
                , BIRTHDAY, TELEPHONENUMBER, LANGUAGE_CODE));
        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
