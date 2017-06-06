package com.commercetools.payment.payone.config;

import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.TransactionType;
import org.junit.Test;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.CREDIT_CARD;
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
        PaymentMethodInfo paymentMethodInfo = configuration.getAvailableMethods().get(CREDIT_CARD);
        assertThat(paymentMethodInfo.getPaymentInterface()).isEqualTo("PAYONE-test");
        assertThat(paymentMethodInfo.getMethod()).isEqualTo("CREDIT_CARD");
        assertThat(paymentMethodInfo.getName().get("de")).isEqualTo("Kreditkarte");
        assertThat(paymentMethodInfo.getName().get("en")).isEqualTo("credit card");

        // validate shorthand method
        assertThat(configuration.getMethodInfo(CREDIT_CARD)).isEqualTo(configuration.getAvailableMethods().get("CREDIT_CARD"));

        // validate default transaction types
        assertThat(configuration.getTransactionTypes().size()).isEqualTo(1);
        assertThat(configuration.getTransactionType(CREDIT_CARD)).isEqualTo(TransactionType.AUTHORIZATION);

        // validiate credit card configuration values
        assertThat(configuration.getCreditCardConfiguration()).isNotNull();
        assertThat(configuration.getCreditCardConfiguration().getJavascriptInclude()).isEqualTo("aUrl");
    }
}