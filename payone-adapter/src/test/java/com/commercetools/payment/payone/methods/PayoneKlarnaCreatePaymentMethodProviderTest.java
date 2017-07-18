package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.impl.CreatePaymentDataImpl;
import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.carts.CartTestImpl;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.types.CustomFieldsDraft;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_INVOICE_KLARNA;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class PayoneKlarnaCreatePaymentMethodProviderTest extends BasePayoneCreatePaymentMethodTest {
    @Before
    public void setUp() throws Exception {
        provider = PayoneKlarnaCreatePaymentMethodProvider.of();
    }

    @Test
    public void createPaymentDraft_withAllValues() throws Exception {
        SoftAssertions sa = new SoftAssertions();

        //test mocks
        Map<String, String> config = new HashMap<>();
        config.put(GENDER, "m");
        config.put(IP, "127.0.0.8");
        config.put(BIRTHDAY, "19851112");
        config.put(TELEPHONENUMBER, "89234579");

        testCart = new CartTestImpl();
        testCart.totalPrice = Monetary.getDefaultAmountFactory().setCurrency("EUR").setNumber(87.65).create();
        testCart.locale = Locale.GERMAN;
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, testCart, "test-klarna-reference-id",
                config, null, null);

        // tested function createPaymentDraft(Cart)
        // because we test protected method, we should make explicit down casting of provider property
        PaymentDraft paymentDraft = ((PayoneKlarnaCreatePaymentMethodProvider) provider)
                .createPaymentDraft(cpd).build();

        // asserts
        assertThat(paymentDraft).isNotNull();

        sa.assertThat(paymentDraft.getAmountPlanned().getNumber().doubleValueExact()).isEqualTo(87.65);
        sa.assertThat(paymentDraft.getAmountPlanned().getCurrency().getCurrencyCode()).isEqualTo("EUR");

        CustomFieldsDraft custom = paymentDraft.getCustom();

        sa.assertThat(custom).isNotNull();

        sa.assertThat(custom.getType().getKey()).isEqualTo(PAYMENT_INVOICE_KLARNA.getValue());

        Map<String, JsonNode> customFields = custom.getFields();

        sa.assertThat(customFields).isNotNull();
        sa.assertThat(customFields.size()).isEqualTo(6);
        sa.assertThat(customFields.get(LANGUAGE_CODE).textValue()).isEqualTo("de");
        sa.assertThat(customFields.get(REFERENCE).textValue()).isEqualTo("test-klarna-reference-id");

        sa.assertThat(customFields.get(GENDER).textValue()).isEqualTo("m");
        sa.assertThat(customFields.get(IP).textValue()).isEqualTo("127.0.0.8");
        sa.assertThat(customFields.get(BIRTHDAY).textValue()).isEqualTo("19851112");
        sa.assertThat(customFields.get(TELEPHONENUMBER).textValue()).isEqualTo("89234579");

        sa.assertAll();
    }

    @Test
    public void createPaymentDraft_withAllMissingValues() throws Exception {
        SoftAssertions sa = new SoftAssertions();

        //test mocks
        Map<String, String> config = new HashMap<>();

        testCart = new CartTestImpl();
        testCart.totalPrice = Monetary.getDefaultAmountFactory().setCurrency("USD").setNumber(111.22).create();
        testCart.locale = Locale.GERMAN;
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, testCart, "test-klarna-reference-id",
                config, null, null);

        // tested function createPaymentDraft(Cart)
        // because we test protected method, we should make explicit down casting of provider property
        PaymentDraft paymentDraft = ((PayoneKlarnaCreatePaymentMethodProvider) provider)
                .createPaymentDraft(cpd).build();

        // asserts
        assertThat(paymentDraft).isNotNull();

        sa.assertThat(paymentDraft.getAmountPlanned().getNumber().doubleValueExact()).isEqualTo(111.22);
        sa.assertThat(paymentDraft.getAmountPlanned().getCurrency().getCurrencyCode()).isEqualTo("USD");

        CustomFieldsDraft custom = paymentDraft.getCustom();

        sa.assertThat(custom).isNotNull();

        sa.assertThat(custom.getType().getKey()).isEqualTo(PAYMENT_INVOICE_KLARNA.getValue());

        Map<String, JsonNode> customFields = custom.getFields();

        sa.assertThat(customFields).isNotNull();
        sa.assertThat(customFields.size()).isEqualTo(6);
        sa.assertThat(customFields.get(LANGUAGE_CODE).textValue()).isEqualTo("de");
        sa.assertThat(customFields.get(REFERENCE).textValue()).isEqualTo("test-klarna-reference-id");

        // mandatory klarna fields exist, but empty
        assertContainsButNull(sa, customFields, GENDER);
        assertContainsButNull(sa, customFields, IP);
        assertContainsButNull(sa, customFields, BIRTHDAY);
        assertContainsButNull(sa, customFields, TELEPHONENUMBER);

        sa.assertAll();
    }

    private static void assertContainsButNull(SoftAssertions sa, Map<String, JsonNode> customFields, String key) {
        sa.assertThat(customFields.containsKey(key))
                .withFailMessage(format("Custom fields must contain key [%s]", key))
                .isTrue();
        sa.assertThat(customFields.get(key))
                .withFailMessage(format("Custom field [%s] expected to be: null,%n      "
                                      + "but was: %s", key, customFields.get(key)))
                .isNull();
    }

}