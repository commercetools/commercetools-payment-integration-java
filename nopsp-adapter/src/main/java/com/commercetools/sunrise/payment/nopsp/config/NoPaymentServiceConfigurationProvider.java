package com.commercetools.sunrise.payment.nopsp.config;

import io.sphere.sdk.json.SphereJsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NoPaymentServiceConfigurationProvider {

    private static final String DEFAULT_CONFIG_PATH = "methods/nopsp.json";

    public static NoPaymentServiceConfigurationProvider of() {
        return new NoPaymentServiceConfigurationProvider(DEFAULT_CONFIG_PATH);
    }
    private final String pathToJson;

    private NoPaymentServiceConfigurationProvider(String pathToJson) {
        this.pathToJson = pathToJson;
    }

    /**
     * Load the configuration from the configured path.
     * @return the payment configuration object deserialized from the configured path
     */
    public NoPaymentServiceConfiguration load() {
        String jsonValue = "";

        try {
            InputStream inputStream = NoPaymentServiceConfigurationProvider.class.getClassLoader().getResourceAsStream(pathToJson);
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
        return SphereJsonUtils.readObject(jsonValue, NoPaymentServiceConfiguration.class);
    }
}
