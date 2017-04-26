package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
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
    public void getLanguageFromPaymentDataOrFallback() throws Exception {
        Map<String, String> config = new HashMap<>();
        CreatePaymentDataImpl cpd = new CreatePaymentDataImpl(null, null, mockCart, null, config, null, null);

        assertThat("Should Fallback to default \"en\", when both cart and custom field are empty",
                PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback(cpd), is("en"));

        config.put(LANGUAGE_CODE, "en");
        assertThat(PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback(cpd), is("en"));

        config.put(LANGUAGE_CODE, "nl");
        assertThat(PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback(cpd), is("nl"));

        when(mockCart.getLocale()).thenReturn(new Locale("uk"));
        assertThat("Should ignore cart value while custom field exists",
                PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback(cpd), is("nl"));

        config.remove(LANGUAGE_CODE);
        assertThat("Should use value from the cart, when custom field is empty",
                PayoneCreatePaymentMethodBase.getLanguageFromPaymentDataOrFallback(cpd), is("uk"));
    }

}