package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import java.time.chrono.ChronoZonedDateTime;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_INVOICE_KLARNA;
import static com.commercetools.payment.payone.utils.PaymentMappingUtil.mapDraftCustomFields;
import static java.util.Arrays.asList;

public class PayoneKlarnaCreatePaymentMethodProvider extends PayoneCreatePaymentMethodBase {

    public static final String KLARNA_ADD_PAYDATA_ACTION_VALUE = "start_session";
    public static final String KLARNA_ACTION = "add_paydata[action]";
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
        mapDraftCustomFields(customFieldsDraftBuilder, cpd, asList(GENDER, IP, BIRTHDAY, TELEPHONENUMBER));
        customFieldsDraftBuilder.addObject(KLARNA_ACTION, KLARNA_ACTION_START);
        return super.createPaymentDraft(cpd)
                .custom(customFieldsDraftBuilder.build());
    }
}
