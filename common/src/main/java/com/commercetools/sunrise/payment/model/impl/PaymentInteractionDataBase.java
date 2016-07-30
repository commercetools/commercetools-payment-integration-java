package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.model.PaymentInteractionData;
import io.sphere.sdk.client.SphereClient;

/**
 * Created by mgatz on 7/20/16.
 */
public abstract class PaymentInteractionDataBase implements PaymentInteractionData {

    private final SphereClient client;

    PaymentInteractionDataBase(final SphereClient client) {
        this.client = client;
    }

    @Override
    public SphereClient getSphereClient() {
        return this.client;
    }

}
