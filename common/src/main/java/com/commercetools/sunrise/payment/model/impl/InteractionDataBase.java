package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.model.InteractionData;
import io.sphere.sdk.client.SphereClient;

/**
 * Created by mgatz on 7/20/16.
 */
public abstract class InteractionDataBase implements InteractionData {

    private SphereClient client;

    InteractionDataBase(SphereClient client) {
        this.client = client;
    }

    @Override
    public SphereClient getSphereClient() {
        return client;
    }
}
