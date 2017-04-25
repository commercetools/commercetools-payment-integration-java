package com.commercetools.payment.payone.config;

import io.sphere.sdk.json.SphereJsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayoneConfigurationProvider {

    private static final String DEFAULT_CONFIG_PATH = "methods/payone.json";

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
        String jsonValue = "";

        try {
            InputStream inputStream = PayoneConfigurationProvider.class.getClassLoader().getResourceAsStream(pathToJson);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();

            String line = reader.readLine();

            while(null != line) {
                builder.append(line);
                line = reader.readLine();
            }

            jsonValue = builder.toString();
        } catch (IOException e) {
            // TODO: add logging here
            e.printStackTrace();
        }
        return SphereJsonUtils.readObject(jsonValue, PayoneConfiguration.class);
    }
}
