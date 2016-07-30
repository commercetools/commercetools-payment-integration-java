package com.commercetools.sunrise.payment.payone.config;

import io.sphere.sdk.payments.TransactionType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by mgatz on 7/27/16.
 */
public class PayoneConfigurationProviderTest {

    @Test
    public void loadConfiguration() {
        PayoneConfigurationProvider provider = PayoneConfigurationProvider.of("methods/payone-test.json");

        assertThat(provider).isNotNull();

        PayoneConfiguration configuration = provider.load();

        assertThat(configuration).isNotNull();
        assertThat(configuration.getInterfaceId()).isEqualTo("PAYONE-test");

        // validate payment method config
        assertThat(configuration.getAvailableMethods().size()).isEqualTo(1);
        assertThat(configuration.getAvailableMethods().get("CREDIT_CARD").getPaymentInterface()).isEqualTo("PAYONE-test");
        assertThat(configuration.getAvailableMethods().get("CREDIT_CARD").getMethod()).isEqualTo("CREDIT_CARD");
        assertThat(configuration.getAvailableMethods().get("CREDIT_CARD").getName().get("de")).isEqualTo("Kreditkarte");
        assertThat(configuration.getAvailableMethods().get("CREDIT_CARD").getName().get("en")).isEqualTo("credit card");

        // validate shorthand method
        assertThat(configuration.getMethodInfo("CREDIT_CARD")).isEqualTo(configuration.getAvailableMethods().get("CREDIT_CARD"));

        // validate default transaction types
        assertThat(configuration.getTransactionTypes().size()).isEqualTo(1);
        assertThat(configuration.getTransactionType("CREDIT_CARD")).isEqualTo(TransactionType.AUTHORIZATION);
    }
}