package com.commercetools.sunrise.payment.payone.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.TransactionType;

import java.util.List;
import java.util.Map;

/**
 * Provides the configuration values of the PayOne payment interface and its methods.
 *
 * Is currently read only.
 *
 * Created by mgatz on 7/26/16.
 */
public class PayoneConfiguration {
    private String interfaceId;
    private PayoneCreditCardConfiguration creditCardConfiguration;
    private String handlePaymentBaseUrl;
    private List<String> enabledMethods;
    private Map<String, PaymentMethodInfo> availableMethods;
    private Map<String, TransactionType> transactionTypes;

    @JsonCreator
    private PayoneConfiguration(@JsonProperty("interfaceId") final String interfaceId,
                                @JsonProperty("enabledMethods") final List<String> enabledMethods,
                                @JsonProperty("availableMethods") final Map<String, PaymentMethodInfo> availableMethods,
                                @JsonProperty("transactionTypes") final Map<String, TransactionType> transactionTypes,
                                @JsonProperty("creditCardConfig") final PayoneCreditCardConfiguration creditCardConfiguration) {
        this.interfaceId = interfaceId;
        this.creditCardConfiguration = creditCardConfiguration;
        this.handlePaymentBaseUrl = handlePaymentBaseUrl;
        this.enabledMethods = enabledMethods;
        this.availableMethods = availableMethods;
        this.transactionTypes = transactionTypes;

    }

    public String getInterfaceId() {

        return interfaceId;
    }

    /**
     * @return the configured base URL of the Payone Connectors handle interface
     */
    public String getHandlePaymentBaseUrl() {

        return handlePaymentBaseUrl;
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

    /**
     * @return all configured {@link TransactionType}
     */
    public Map<String, TransactionType> getTransactionTypes() {
        return transactionTypes;
    }

    /**
     * Get the configured {@link TransactionType} for the passed method Id
     * @param methodId the method Id
     * @return the configuration {@link TransactionType}
     */
    public TransactionType getTransactionType(String methodId) {
        return transactionTypes.get(methodId);
    }

    /**
     * Get the credit card related configuration values.
     * @return the payone credit card configuration values
     */
    public PayoneCreditCardConfiguration getCreditCardConfiguration() {
        return creditCardConfiguration;
    }
}
