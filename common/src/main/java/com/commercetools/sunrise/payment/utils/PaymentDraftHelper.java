package com.commercetools.sunrise.payment.utils;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import io.sphere.sdk.payments.PaymentDraftBuilder;

/**
 * Created by mgatz on 7/10/16.
 */
public interface PaymentDraftHelper {
    /**
     * Create a default payment draft builder that is preconfigured with all values that do not differ between the payment
     * methods.
     * @param data the payment creation data object providing all neccessary data
     * @return a {@link PaymentDraftBuilder} that can be enhanced with method specific data
     */
    public static PaymentDraftBuilder createPaymentDraftBuilder(CreatePaymentData data) {
        PaymentDraftBuilder builder = PaymentDraftBuilder.of(data.getCart().getTotalPrice());

        if(data.getCustomer().isPresent()) {
            builder.customer(data.getCustomer().get());
        }

        return builder;
    }
}
