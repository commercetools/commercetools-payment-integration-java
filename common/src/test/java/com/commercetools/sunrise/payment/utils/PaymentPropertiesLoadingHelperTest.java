package com.commercetools.sunrise.payment.utils;

import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by mgatz on 7/19/16.
 */
public class PaymentPropertiesLoadingHelperTest {

    private static final String TEST_FILE_PATH = "testPayment.properties";

    private static final String TEST_PSP_ID = "test";
    private static final String TEST_METHODID = "test-methodid";
    private static final int TEST_METHOD_COUNT = 1;
    private static final String LANGUAGE_TAG_EN = "en";
    private static final String LANGUAGE_TAG_DE = "de";
    private static final String TEST_PAYMENT_INTERFACE = "test";

    private PaymentPropertiesLoadingHelper helper;

    @Before
    public void setup() {
        helper = PaymentPropertiesLoadingHelper.createFromResource(TEST_FILE_PATH);
    }

    @Test
    public void createFromResource() throws Exception {
        assertNotNull(helper);
        assertThat(helper.getProperty("methods.interface"), is(TEST_PAYMENT_INTERFACE));
    }

    public void getPaymentServiceProviderId() {
        assertEquals(TEST_PSP_ID, helper.getPaymentServiceId());
    }

    @Test
    public void getPaymentMethods() {
        List<String> methodIds = helper.getAvaiableMethodIds();

        assertNotNull(methodIds);
        assertThat(methodIds.size(), is(TEST_METHOD_COUNT));
        assertThat(methodIds.get(0), is(TEST_METHODID));
    }

    @Test
    public void getPaymentMethodInfo() {
        PaymentMethodInfo pmi = helper.getMethodInfo(TEST_METHODID);

        assertNotNull(pmi);
        assertThat(pmi.getPaymentInterface(), is(TEST_PAYMENT_INTERFACE));
        assertThat(pmi.getMethod(), is(TEST_METHODID));
        assertThat(pmi.getName().get(LANGUAGE_TAG_EN), is("test method"));
        assertThat(pmi.getName().get(LANGUAGE_TAG_DE), is("Testmethode"));
    }
}