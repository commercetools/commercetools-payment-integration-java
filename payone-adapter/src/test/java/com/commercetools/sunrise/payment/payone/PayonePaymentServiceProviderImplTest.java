package com.commercetools.sunrise.payment.payone;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by mgatz on 7/18/16.
 */
public class PayonePaymentServiceProviderImplTest {
    @Test
    public void getId() throws Exception {
        assertThat(new PayonePaymentServiceProviderImpl().getId(), is("payone"));
    }

    @Test
    public void getAvailablePaymentMethods() throws Exception {
        assertTrue(true); // TODO: implement
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