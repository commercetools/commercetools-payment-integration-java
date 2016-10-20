package com.commercetools.sunrise.payment.payone.methods;

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
import java.util.Locale;
import java.util.Map;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static com.commercetools.sunrise.payment.payone.methods.PayonePaymentMethodType.PAYMENT_WALLET;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by akovalenko on 20/10/16.
 */
public class PayonePaypalCreatePaymentMethodProviderTest extends BasePayoneCreatePaymentMethodTest {
    private PayonePaypalCreatePaymentMethodProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = PayonePaypalCreatePaymentMethodProvider.of();
    }

    @Test
    public void createPaymentDraft() throws Exception {
        SoftAssertions sa = new SoftAssertions();

        //test mocks
        Map<String, String> config = new HashMap<>();
        config.put(LANGUAGE_CODE, "de");
        config.put(SUCCESS_URL, "http://success.com");
        config.put(ERROR_URL, "http://error.com");
        config.put(CANCEL_URL, "http://cancel.com");

        testCart = new CartTestImpl();
        testCart.totalPrice = Monetary.getDefaultAmountFactory().setCurrency("CAD").setNumber(48.58).create();
        testCart.locale = Locale.CHINA;
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, testCart, "hack",
                config, null, null);

        // tested function
        PaymentDraft paymentDraft = provider.createPaymentDraft(cpd).build();

        // asserts
        assertThat(paymentDraft).isNotNull();

        CustomFieldsDraft custom = paymentDraft.getCustom();

        sa.assertThat(custom.getType().getKey()).isEqualTo(PAYMENT_WALLET.getValue());

        Map<String, JsonNode> customFields = custom.getFields();

        sa.assertThat(paymentDraft.getAmountPlanned().getNumber().doubleValueExact()).isEqualTo(48.58);
        sa.assertThat(paymentDraft.getAmountPlanned().getCurrency().getCurrencyCode()).isEqualTo("CAD");

        sa.assertThat(customFields.get(REFERENCE).textValue()).isEqualTo("hack");
        sa.assertThat(customFields.get(LANGUAGE_CODE).textValue())
                .describedAs("If both cart and config language are set - config value has precedence")
                .isEqualTo("de");

        sa.assertThat(customFields.get(SUCCESS_URL).textValue()).isEqualTo("http://success.com");
        sa.assertThat(customFields.get(ERROR_URL).textValue()).isEqualTo("http://error.com");
        sa.assertThat(customFields.get(CANCEL_URL).textValue()).isEqualTo("http://cancel.com");

        sa.assertAll();
    }

}