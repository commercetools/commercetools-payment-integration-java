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

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REFERENCE;
import static com.commercetools.payment.payone.methods.PayonePaymentMethodType.PAYMENT_CASH_ADVANCE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by akovalenko on 20/10/16.
 */
public class PayoneBanktransferInAdvanceCreatePaymentProviderTest extends BasePayoneCreatePaymentMethodTest {

    @Before
    public void setUp() throws Exception {
        provider = PayoneBanktransferInAdvanceCreatePaymentProvider.of();
    }

    @Test
    public void createPaymentDraft() throws Exception {
        SoftAssertions sa = new SoftAssertions();

        //test mocks
        Map<String, String> config = new HashMap<>();

        testCart = new CartTestImpl();
        testCart.totalPrice = Monetary.getDefaultAmountFactory().setCurrency("UAH").setNumber(123.21).create();
        testCart.locale = Locale.FRANCE;
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, testCart, "foo-bar",
                config, null, null);

        // tested function createPaymentDraft(Cart)
        // because we test protected method, we should make explicit down casting of provider property
        PaymentDraft paymentDraft = ((PayoneBanktransferInAdvanceCreatePaymentProvider) provider)
                .createPaymentDraft(cpd).build();

        // asserts
        assertThat(paymentDraft).isNotNull();

        CustomFieldsDraft custom = paymentDraft.getCustom();

        sa.assertThat(custom.getType().getKey()).isEqualTo(PAYMENT_CASH_ADVANCE.getValue());

        Map<String, JsonNode> customFields = custom.getFields();

        sa.assertThat(paymentDraft.getAmountPlanned().getNumber().doubleValueExact()).isEqualTo(123.21);
        sa.assertThat(paymentDraft.getAmountPlanned().getCurrency().getCurrencyCode()).isEqualTo("UAH");

        sa.assertThat(customFields.get(REFERENCE).textValue()).isEqualTo("foo-bar");
        sa.assertThat(customFields.get(LANGUAGE_CODE).textValue()).describedAs("Should use value from cart")
                .isEqualTo(Locale.FRANCE.getLanguage());

        sa.assertAll();
    }

}