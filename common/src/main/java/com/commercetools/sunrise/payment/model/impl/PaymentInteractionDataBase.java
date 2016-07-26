package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.model.PaymentInteractionData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.PaymentMethodInfo;

/**
 * Created by mgatz on 7/20/16.
 */
public abstract class PaymentInteractionDataBase implements PaymentInteractionData {

    private final SphereClient client;
    private final PaymentMethodInfo paymentMethodInfo;

    PaymentInteractionDataBase(final SphereClient client,final PaymentMethodInfo paymentMethodInfo) {
        this.client = client;
        this.paymentMethodInfo = paymentMethodInfo;
    }

    @Override
    public SphereClient getSphereClient() {
        return this.client;
    }

    @Override
    public PaymentMethodInfo getPaymentMethodinInfo() {
        return this.paymentMethodInfo;
    }
}
