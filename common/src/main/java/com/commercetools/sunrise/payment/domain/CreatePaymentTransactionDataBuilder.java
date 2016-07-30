package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.impl.CreatePaymentTransactionDataImpl;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.TransactionType;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder to build a {@link com.commercetools.sunrise.payment.model.CreatePaymentTransactionData} object.
 * Created by mgatz on 7/10/16.
 */
public class CreatePaymentTransactionDataBuilder {
    private final SphereClient client;
    private final String paymentRef;
    private Map<String, String> config = new HashMap<>();
    private TransactionType transactionType;

    private CreatePaymentTransactionDataBuilder(final SphereClient client, final String paymentRef) {
        this.client = client;
        this.paymentRef = paymentRef;
    }

    public static CreatePaymentTransactionDataBuilder of(final SphereClient client,
                                                         final String paymentRef) {
        return new CreatePaymentTransactionDataBuilder(
                client,
                paymentRef);
    }

    public CreatePaymentTransactionDataBuilder setTransactionType(TransactionType type) {
        this.transactionType = type;
        return this;
    }

    /**
     * Set a configuration value passed to the handling method.
     * @param name the configuration name
     * @param value the value
     * @return this object
     */
    public CreatePaymentTransactionDataBuilder setConfigValue(String name, String value) {
        this.config.put(name, value);
        return this;
    }

    /**
     * Create a new instance of {@link com.commercetools.sunrise.payment.model.CreatePaymentTransactionData} using the provided data.
     * @return immutable data object
     */
    public CreatePaymentTransactionDataImpl build() {
        CreatePaymentTransactionDataImpl transactionData = new CreatePaymentTransactionDataImpl(client, paymentRef, config);
        transactionData.setTransactionType(transactionType);
        return transactionData;
    }
}
