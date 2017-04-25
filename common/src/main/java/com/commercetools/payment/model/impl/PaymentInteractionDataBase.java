package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.PaymentInteractionData;
import io.sphere.sdk.client.SphereClient;

import java.util.Map;

/**
 * Created by mgatz on 7/20/16.
 */
public abstract class PaymentInteractionDataBase implements PaymentInteractionData {

    private final SphereClient client;
    private final Map<String, String> config;

    PaymentInteractionDataBase(final SphereClient client, Map<String, String> config) {
        this.client = client;
        this.config = config;
    }

    @Override
    public SphereClient getSphereClient() {
        return this.client;
    }

    @Override
    public String getConfigByName(String name) {
        return config.get(name);
    }
}
