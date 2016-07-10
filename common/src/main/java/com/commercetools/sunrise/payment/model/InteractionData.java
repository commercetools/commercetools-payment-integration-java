package com.commercetools.sunrise.payment.model;

import io.sphere.sdk.client.SphereClient;

/**
 * This base interface ensures that all actions taken by the adapter
 * can use a provided {@link SphereClient} instance and do not have to take care of creating one on their own.
 *
 * Created by mgatz on 7/20/16.
 */
public interface InteractionData {
    /**
     * @return a shop provided valid sphere client
     */
    SphereClient getSphereClient();
}
