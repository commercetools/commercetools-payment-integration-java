package com.commercetools.payment.payone.methods;

import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.impl.CreatePaymentDataImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.client.InvalidClientCredentialsException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.commercetools.payment.actions.OperationResult.FAILED;
import static com.commercetools.payment.actions.ShopAction.HANDLE_ERROR;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayoneCreatePaymentMethodBaseTest {
    @Mock
    private Cart mockCart;

    /**
     * Testing default implementation from abstract class, so call real methods.
     */
    @Mock(answer = CALLS_REAL_METHODS)
    private PayoneCreatePaymentMethodBase mockCreatePayment;

    @Mock
    private SphereClient mockSphereClient;

    @Mock
    private PaymentMethodInfo mockPaymentMethodInfo;

    @Mock
    private Payment payment;

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

    @Test
    public void create_withSphereClientException() throws Exception {
        when(mockCreatePayment.getMethodType()).thenReturn("test method type");

        // sphere client with fake credentials
        SphereClient sphereClient = SphereClientFactory.of().createClient("testProjectKey", "b", "c");

        CreatePaymentData createPaymentData = new CreatePaymentDataImpl(sphereClient, mockPaymentMethodInfo,
                mockCart, "test-reference", emptyMap(), null, null);

        PaymentCreationResult join = mockCreatePayment.create().apply(createPaymentData)
                .toCompletableFuture().join();

        assertThat(join, is(notNullValue()));
        assertThat(join.getHandlingTask().getAction(), is(HANDLE_ERROR));
        assertThat(join.getOperationResult(), is(FAILED));
        assertThat(join.getMessage().orElse(""), containsString("test method type"));
        assertThat(join.getException().orElse(null), is(instanceOf(CompletionException.class)));
        assertThat(join.getException().map(Throwable::getCause).orElse(null),
                is(instanceOf(InvalidClientCredentialsException.class)));
        assertThat(join.getException().map(Throwable::getCause).map(Throwable::getMessage).orElse(null),
                containsString("invalid_client"));

    }

    @Test
    public void create_withCustomException() throws Exception {
        when(mockCreatePayment.getMethodType()).thenReturn("test method type2");

        CreatePaymentData createPaymentData = new CreatePaymentDataImpl(mockSphereClient, mockPaymentMethodInfo,
                mockCart, "test-reference", emptyMap(), null, null);

        when(mockSphereClient.execute(any())).thenReturn(CompletableFuture.completedFuture(payment));
        // simulate an exception when cart update in the execution chain is failed
        when(mockSphereClient.execute(any(CartUpdateCommand.class)))
                .thenThrow(new RuntimeException("cart failed test exception"));

        PaymentCreationResult join = mockCreatePayment.create().apply(createPaymentData)
                .toCompletableFuture().join();

        assertThat(join, is(notNullValue()));
        assertThat(join.getHandlingTask().getAction(), is(HANDLE_ERROR));
        assertThat(join.getOperationResult(), is(FAILED));
        assertThat(join.getMessage().orElse(""), containsString("test method type2"));
        assertThat(join.getException().orElse(null), is(notNullValue()));
        assertThat(join.getException().map(Throwable::getMessage).orElse(null),
                containsString("cart failed test exception"));

    }
}