package com.commercetools.sunrise.payment.utils;

import io.sphere.sdk.payments.PaymentMethodInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Provides helper methods to easily parse a payment method configuration file.
 * Created by mgatz on 7/19/16.
 */
public interface PaymentPropertiesLoadingHelper {
    static PaymentPropertiesLoadingHelper createFromResource(String path) {
        Properties p = new Properties();
        InputStream inputStream = PaymentPropertiesLoadingHelper.class.getClassLoader().getResourceAsStream(path);

        try {
            p.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: add logging here
        }

        return new PaymentPropertiesLoadingHelperImpl(p);
    }

    /**
     * Simple wrapper method that calls the appropriate method of the underlying @{@link Properties} object.
     * @param name the property key
     * @return the property value
     */
    String getProperty(String name);

    /**
     * Get a list of all available payment method IDs from the underlying configuration object.
     * @return list of payment method IDs
     */
    List<String> getAvaiableMethodIds();

    /**
     * Create a {@link PaymentMethodInfo} object of the underlying configuration object.
     * @param methodId the method ID to get the information for
     * @return a {@link PaymentMethodInfo} object
     */
    PaymentMethodInfo getMethodInfo(String methodId);

    /**
     * Get the payment service Id this properties belong to.
     * @return the payment service provider Id
     */
    String getPaymentServiceId();
}
