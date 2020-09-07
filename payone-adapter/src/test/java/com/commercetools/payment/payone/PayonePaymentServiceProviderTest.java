package com.commercetools.payment.payone;

import com.commercetools.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Before;
import org.junit.Test;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderTest {
    public static final String METHOD_ID = "PAYONE";

    private PaymentServiceProvider psp;

    @Before
    public void setUp() throws Exception {
        psp = new PayonePaymentServiceProvider();
    }

    @Test
    public void getId() throws Exception {
        assertThat(psp.getId()).isEqualTo(METHOD_ID);
    }

    @Test
    public void getAvailablePaymentMethods() throws Exception {
        assertThat(psp.getAvailablePaymentMethods().size()).isEqualTo(10);

        PaymentMethodInfo pmi = psp.getAvailablePaymentMethods().get(0); // credit card
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(CREDIT_CARD);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Credit Card");
        assertThat(pmi.getName().get("de")).isEqualTo("Kreditkarte");
        assertThat(pmi.getName().get("fr")).isEqualTo("Carte bancaire");

        pmi = psp.getAvailablePaymentMethods().get(1); // paypal
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(WALLET_PAYPAL);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Paypal");
        assertThat(pmi.getName().get("de")).isEqualTo("Paypal");
        assertThat(pmi.getName().get("fr")).isEqualTo("Paypal");


        pmi = psp.getAvailablePaymentMethods().get(2); // paydirekt
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(WALLET_PAYDIREKT);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Paydirekt");
        assertThat(pmi.getName().get("de")).isEqualTo("Paydirekt");
        assertThat(pmi.getName().get("fr")).isEqualTo("Paydirekt");

        pmi = psp.getAvailablePaymentMethods().get(3); // sofort ueberweisung
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_SOFORTUEBERWEISUNG);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Sofortüberweisung");
        assertThat(pmi.getName().get("de")).isEqualTo("Sofortüberweisung");
        assertThat(pmi.getName().get("fr")).isEqualTo("Sofortüberweisung");

        pmi = psp.getAvailablePaymentMethods().get(4); // ideal
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_IDEAL);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("iDEAL");
        assertThat(pmi.getName().get("de")).isEqualTo("iDEAL");
        assertThat(pmi.getName().get("fr")).isEqualTo("iDEAL");

        pmi = psp.getAvailablePaymentMethods().get(5); // Banconcat
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_BANCONTACT);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Bancontact");
        assertThat(pmi.getName().get("de")).isEqualTo("Bancontact");
        assertThat(pmi.getName().get("fr")).isEqualTo("Bancontact");

        pmi = psp.getAvailablePaymentMethods().get(6); // prepaid
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_ADVANCE);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("de")).isEqualTo("Vorkasse");
        assertThat(pmi.getName().get("en")).isEqualTo("Prepaid");
        assertThat(pmi.getName().get("fr")).isEqualTo("Paiement à l’avance");

        pmi = psp.getAvailablePaymentMethods().get(7); // post finance efinance
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_POSTFINANCE_EFINANCE);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Postfinance E-Finance");
        assertThat(pmi.getName().get("de")).isEqualTo("Postfinance E-Finance");
        assertThat(pmi.getName().get("fr")).isEqualTo("Postfinance E-Finance");

        pmi = psp.getAvailablePaymentMethods().get(8); // postfinance e-card
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(BANK_TRANSFER_POSTFINANCE_CARD);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Postfinance Card");
        assertThat(pmi.getName().get("de")).isEqualTo("Postfinance Card");
        assertThat(pmi.getName().get("fr")).isEqualTo("Postfinance Card");

        pmi = psp.getAvailablePaymentMethods().get(9); // klarna
        assertThat(pmi.getPaymentInterface()).isEqualTo(METHOD_ID);
        assertThat(pmi.getMethod()).isEqualTo(INVOICE_KLARNA);
        assertThat(pmi.getName().getLocales().size()).isEqualTo(3);
        assertThat(pmi.getName().get("en")).isEqualTo("Pay on delivery");
        assertThat(pmi.getName().get("de")).isEqualTo("Kauf auf Rechnung");
        assertThat(pmi.getName().get("fr")).isEqualTo("Paiement sur facture");
    }

    @Test
    public void provideCreatePaymentHandler() throws Exception {
        assertThat(true).isTrue(); // TODO: implement
    }

    @Test
    public void provideCreatePaymentHandler_exception() throws Exception {
        assertThatThrownBy(() -> psp.provideCreatePaymentHandler("blah-blah"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("blah-blah");
    }

    @Test
    public void provideCreatePaymentTransactionHandler_exception() throws Exception {
        assertThatThrownBy(() -> psp.provideCreatePaymentTransactionHandler("ya-hoooo"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("ya-hoooo");
    }

}