package com.commercetools.sunrise.payment.nopsp.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.TransactionType;

import java.util.List;
import java.util.Map;

/**
 * Provides the configuration values of the default payment interface and its methods.
 *
 * Is currently read only.
 */
public class NoPaymentServiceConfiguration {
    private String interfaceId;
    private List<String> enabledMethods;
    private Map<String, PaymentMethodInfo> availableMethods;

    @JsonCreator
    private NoPaymentServiceConfiguration(@JsonProperty("interfaceId") final String interfaceId,
                                          @JsonProperty("enabledMethods") final List<String> enabledMethods,
                                          @JsonProperty("availableMethods") final Map<String, PaymentMethodInfo> availableMethods) {
        this.interfaceId = interfaceId;
        this.enabledMethods = enabledMethods;
        this.availableMethods = availableMethods;

    }

    public String getInterfaceId() {

        return interfaceId;
    }

    /**
     * @return the list of enabled payment methods
     */
    public List<String> getEnabledMethods() {

        return enabledMethods;
    }

    /**
     * @return the available payment method configurations mapped by their method ID
     */
    public Map<String, PaymentMethodInfo> getAvailableMethods() {

        return availableMethods;
    }

    /**
     * @param methodId the method id to check
     * @return true if the method is enabled
     */
    public boolean isMethodEnabled(String methodId) {

        return this.enabledMethods.contains(methodId);
    }

    /**
     * Try finding the {@link PaymentMethodInfo} for the passed method ID.
     * @param methodId the method ID
     * @return the {@link PaymentMethodInfo} or null if none is found
     */
    public PaymentMethodInfo getMethodInfo(String methodId) {

        return this.availableMethods.get(methodId);
    }
}
