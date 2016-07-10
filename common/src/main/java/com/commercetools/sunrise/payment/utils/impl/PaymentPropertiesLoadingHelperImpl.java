package com.commercetools.sunrise.payment.utils.impl;

import com.commercetools.sunrise.payment.utils.PaymentPropertiesLoadingHelper;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mgatz on 7/12/16.
 */
public class PaymentPropertiesLoadingHelperImpl implements PaymentPropertiesLoadingHelper {

    private static final String PROP_KEY_SERVICE_ID = "methods.interface";
    private static final String PROP_KEY_METHODS_AVAILABLE = "methods.available";
    private Properties properties;

    public PaymentPropertiesLoadingHelperImpl(Properties p) {
        this.properties = p;
    }

    @Override
    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    @Override
    public List<String> getAvaiableMethodIds() {
        return new ArrayList<>(Arrays.asList(getProperty(PROP_KEY_METHODS_AVAILABLE).split(",")));
    }

    @Override
    public PaymentMethodInfo getMethodInfo(String methodId) {

        String prefix = "methods." + methodId + ".";

        Set<Object> keys = properties.keySet().stream().filter(key -> key.toString().startsWith(prefix + "name.")).collect(Collectors.toSet());

        // load localized values
        LocalizedString localizedName = LocalizedString.ofStringToStringMap(
                properties.keySet().stream()
                        .map(key -> key.toString())
                        .filter(key -> key.startsWith(prefix + "name."))
                        .collect(Collectors.toMap(
                                key -> key.substring(key.lastIndexOf('.') + 1),
                                key -> properties.getProperty(key))
                        ));

        return PaymentMethodInfoBuilder.of()
                .paymentInterface(properties.getProperty(PROP_KEY_SERVICE_ID))
                .method(methodId)
                .name(localizedName)
                .build();
    }

    @Override
    public String getPaymentServiceId() {
        return getProperty("methods.interface");
    }
}
