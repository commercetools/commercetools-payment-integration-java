package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.domain.InteractionData;
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
