package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderImplTest {

    private static final String METHOD_NAME_CC = "PayoneCC";
    private static final String METHOD_NAME_PAYPAL = "PayonePP";
    private static final String METHOD_NAME_SOFORT = "PayoneSB";
    public static final String METHOD_ID = "Payone";

    @Test
    public void getId() throws Exception {
        assertThat(new PayonePaymentServiceProviderImpl().getId(), is(METHOD_ID));
    }

    @Test
    public void getAvailablePaymentMethods() throws Exception {
        PaymentServiceProvider psp = new PayonePaymentServiceProviderImpl();
        assertThat(psp.getAvailablePaymentMethods().size(), is(3));

        PaymentMethodInfo pmi = psp.getAvailablePaymentMethods().get(0); // credit card
        assertThat(pmi.getPaymentInterface(), is(METHOD_ID));
        assertThat(pmi.getMethod(), is(METHOD_NAME_CC));
        assertThat(pmi.getName().getLocales().size(), is(2));
        assertThat(pmi.getName().get("en"), is("Credit Card"));
        assertThat(pmi.getName().get("de"), is("Kreditkarte"));

        pmi = psp.getAvailablePaymentMethods().get(1); // paypal
        assertThat(pmi.getPaymentInterface(), is(METHOD_ID));
        assertThat(pmi.getMethod(), is(METHOD_NAME_PAYPAL));
        assertThat(pmi.getName().getLocales().size(), is(1));
        assertThat(pmi.getName().get("en"), is("Paypal"));

        pmi = psp.getAvailablePaymentMethods().get(2); // Sofortüberweisung
        assertThat(pmi.getPaymentInterface(), is(METHOD_ID));
        assertThat(pmi.getMethod(), is(METHOD_NAME_SOFORT));
        assertThat(pmi.getName().getLocales().size(), is(1));
        assertThat(pmi.getName().get("en"), is("Sofortüberweisung"));
    }

    @Test
    public void provideCreatePaymentHandler() throws Exception {
        assertTrue(true); // TODO: implement
    }

    @Test
    public void provideGetPaymentStatusHandler() throws Exception {
        assertTrue(true); // TODO: implement
    }

}