package com.commercetools.sunrise.payment.payone.config;

import io.sphere.sdk.json.SphereJsonUtils;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayoneConfigurationProvider {

    private static final String DEFAULT_CONFIG_PATH = "META-INF/methods/payone.json";

    public static PayoneConfigurationProvider of() {
        return new PayoneConfigurationProvider(DEFAULT_CONFIG_PATH);
    }

    public static PayoneConfigurationProvider of(String pathToJSON) {
        return new PayoneConfigurationProvider(pathToJSON);
    }

    private final String pathToJson;

    private PayoneConfigurationProvider(String pathToJson) {
        this.pathToJson = pathToJson;
    }

    /**
     * Load the configuration from the configured path.
     * @return the payment configuration object deserialized from the configured path
     */
    public PayoneConfiguration load() {
        return SphereJsonUtils.readObjectFromResource(pathToJson, PayoneConfiguration.class);
    }
}
