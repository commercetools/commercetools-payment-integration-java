package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderTest {

    private static final String METHOD_NAME_CC = "CREDIT_CARD";
    private static final String METHOD_NAME_PAYPAL = "WALLET-PAYPAL";
    private static final String METHOD_NAME_SOFORT = "BANK_TRANSFER-SOFORTUEBERWEISUNG";
    private static final String METHOD_NAME_VOR = "BANK_TRANSFER-ADVANCE";
    public static final String METHOD_ID = "PAYONE";

    @Test
    public void getId() throws Exception {
        assertThat(new PayonePaymentServiceProvider().getId()).isEqualTo(METHOD_ID);
    }

    @Test
    public void getAvailablePaymentMethods() throws Exception {
        PaymentServiceProvider psp = new PayonePaymentServiceProvider();
        assertThat(psp.getAvailablePaymentMethods().size()).isEqualTo(3);

        PaymentMethodInfo pmi = psp.getAvailablePaymentMethods().get(0); // credit card
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(METHOD_NAME_CC);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(2);
        assertThat(pmi.getName().get("en")).isEqualTo("Credit Card");
        assertThat(pmi.getName().get("de")).isEqualTo("Kreditkarte");

        pmi = psp.getAvailablePaymentMethods().get(1); // paypal
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(METHOD_NAME_PAYPAL);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(2);
        assertThat(pmi.getName().get("en")).isEqualTo("Paypal");

        pmi = psp.getAvailablePaymentMethods().get(2); // prepaid
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(METHOD_NAME_VOR);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(2);
        assertThat(pmi.getName().get("de")).isEqualTo("Vorkasse");
        assertThat(pmi.getName().get("en")).isEqualTo("Prepaid");

        /*
        Currently disabled
        pmi = psp.getAvailablePaymentMethods().get(2); // Sofortüberweisung
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(METHOD_NAME_SOFORT);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(1);
        assertThat(pmi.getName().get("en")).isEqualTo("Sofortüberweisung");
        */
    }

    @Test
    public void provideCreatePaymentHandler() throws Exception {
        assertThat(true).isTrue(); // TODO: implement
    }

    @Test
    public void provideGetPaymentStatusHandler() throws Exception {
        assertThat(true).isTrue(); // TODO: implement
    }

}