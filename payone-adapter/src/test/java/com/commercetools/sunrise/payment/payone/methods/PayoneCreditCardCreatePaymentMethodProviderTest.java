package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.impl.CreatePaymentDataImpl;
import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.carts.CartTestImpl;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.types.CustomFieldsDraft;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;

import javax.money.Monetary;
import java.util.HashMap;
import java.util.Map;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_CREDIT_CARD;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by akovalenko on 19/10/16.
 */
public class PayoneCreditCardCreatePaymentMethodProviderTest extends BasePayoneCreatePaymentMethodTest {

    protected PayoneCreditCardCreatePaymentMethodProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = PayoneCreditCardCreatePaymentMethodProvider.of();
    }

    @Test
    public void createPaymentDraft() throws Exception {
        SoftAssertions sa = new SoftAssertions();

        //test mocks
        Map<String, String> config = new HashMap<>();
        config.put(LANGUAGE_CODE, "de");
        config.put(CREDIT_CARD_FORCE_3D_SECURE, "true");
        config.put(SUCCESS_URL, "http://success.com");
        config.put(ERROR_URL, "http://error.com");
        config.put(CANCEL_URL, "http://cancel.com");
        config.put(CREDIT_CARD_CARD_DATA_PLACEHOLDER, "223123");
        config.put(CREDIT_CARD_TRUNCATED_CARD_NUMBER, "433*****345");
        config.put(CREDIT_CARD_CARD_NETWORK, "asdfghjk");
        config.put(CREDIT_CARD_EXPIRY_DATE, "20/20");
        config.put(CREDIT_CARD_CARD_HOLDER_NAME, "Jon Doe");

        testCart = new CartTestImpl();
        testCart.totalPrice = Monetary.getDefaultAmountFactory().setCurrency("EUR").setNumber(42.42).create();
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, testCart, "test-reference-id",
                config, null, null);

        // tested function
        PaymentDraft paymentDraft = provider.createPaymentDraft(cpd).build();

        // asserts
        assertThat(paymentDraft).isNotNull();

        CustomFieldsDraft custom = paymentDraft.getCustom();

        sa.assertThat(custom.getType().getKey()).isEqualTo(PAYMENT_CREDIT_CARD.getValue());

        Map<String, JsonNode> customFields = custom.getFields();

        sa.assertThat(paymentDraft.getAmountPlanned().getNumber().doubleValueExact()).isEqualTo(42.42);
        sa.assertThat(paymentDraft.getAmountPlanned().getCurrency().getCurrencyCode()).isEqualTo("EUR");

        sa.assertThat(customFields.get(REFERENCE).textValue()).isEqualTo("test-reference-id");
        sa.assertThat(customFields.get(LANGUAGE_CODE).textValue()).isEqualTo("de");

        sa.assertThat(customFields.get(CREDIT_CARD_FORCE_3D_SECURE).isBoolean()).isTrue();
        sa.assertThat(customFields.get(CREDIT_CARD_FORCE_3D_SECURE).booleanValue()).isTrue();

        sa.assertThat(customFields.get(SUCCESS_URL).textValue()).isEqualTo("http://success.com");
        sa.assertThat(customFields.get(ERROR_URL).textValue()).isEqualTo("http://error.com");
        sa.assertThat(customFields.get(CANCEL_URL).textValue()).isEqualTo("http://cancel.com");
        sa.assertThat(customFields.get(CREDIT_CARD_CARD_DATA_PLACEHOLDER).textValue()).isEqualTo("223123");
        sa.assertThat(customFields.get(CREDIT_CARD_TRUNCATED_CARD_NUMBER).textValue()).isEqualTo("433*****345");
        sa.assertThat(customFields.get(CREDIT_CARD_CARD_NETWORK).textValue()).isEqualTo("asdfghjk");
        sa.assertThat(customFields.get(CREDIT_CARD_EXPIRY_DATE).textValue()).isEqualTo("20/20");
        sa.assertThat(customFields.get(CREDIT_CARD_CARD_HOLDER_NAME).textValue()).isEqualTo("Jon Doe");

        sa.assertAll();
    }


}