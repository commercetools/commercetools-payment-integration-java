package com.commercetools.sunrise.payment.utils;

import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(helper).isNotNull();
        assertThat(helper.getProperty("methods.interface")).isEqualTo(TEST_PAYMENT_INTERFACE);
    }

    @Test
    public void getPaymentServiceProviderId() {
        assertThat(helper.getPaymentServiceId()).isEqualTo(TEST_PSP_ID);
    }

    @Test
    public void getPaymentMethods() {
        List<String> methodIds = helper.getAvaiableMethodIds();

        assertThat(methodIds).isNotNull();
        assertThat(methodIds.size()).isEqualTo(TEST_METHOD_COUNT);
        assertThat(methodIds.get(0)).isEqualTo(TEST_METHODID);
    }

    @Test
    public void getPaymentMethodInfo() {
        PaymentMethodInfo pmi = helper.getMethodInfo(TEST_METHODID);

        assertThat(pmi).isNotNull();
        assertThat(pmi.getPaymentInterface()).isEqualTo(TEST_PAYMENT_INTERFACE);
        assertThat(pmi.getMethod()).isEqualTo(TEST_METHODID);
        assertThat(pmi.getName().get(LANGUAGE_TAG_EN)).isEqualTo("test method");
        assertThat(pmi.getName().get(LANGUAGE_TAG_DE)).isEqualTo("Testmethode");
    }
}