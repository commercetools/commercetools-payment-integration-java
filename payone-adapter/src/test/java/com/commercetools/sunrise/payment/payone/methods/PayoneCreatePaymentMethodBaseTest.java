package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static com.commercetools.sunrise.payment.payone.methods.PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by akovalenko on 20/10/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PayoneCreatePaymentMethodBaseTest {
    @Mock
    private Cart mockCart;

    @Test
    public void getLanguageFromPaymentDataOrFallbackTest() throws Exception {
        Map<String, String> config = new HashMap<>();
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, mockCart, null, config, null, null);

        assertThat("Should Fallback to default \"en\", when both cart and custom field are empty",
                getLanguageFromPaymentDataOrFallback(cpd), is("en"));

        config.put(LANGUAGE_CODE, "en");
        assertThat(getLanguageFromPaymentDataOrFallback(cpd), is("en"));

        config.put(LANGUAGE_CODE, "nl");
        assertThat(getLanguageFromPaymentDataOrFallback(cpd), is("nl"));

        when(mockCart.getLocale()).thenReturn(new Locale("uk"));
        assertThat("Should ignore cart value while custom field exists",
                getLanguageFromPaymentDataOrFallback(cpd), is("nl"));

        config.remove(LANGUAGE_CODE);
        assertThat("Should use value from the cart, when custom field is empty",
                getLanguageFromPaymentDataOrFallback(cpd), is("uk"));
    }

}