package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.model.HttpRequestInfo;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgatz on 7/20/16.
 */
public class CreatePaymentDataTest {

    private static final String CLIENT_TO_STRING = "foo";
    private static final String CART_TO_STRING = "bar";
    private static final String CUSTOMER_TO_STRING = "hoe";
    private static final String REQUEST_TO_STRING = "woo";

    @Mock
    private SphereClient mockClient;
    @Mock
    private Cart mockCart;
    @Mock
    private Customer mockCustomer;
    @Mock
    private HttpRequestInfo mockRequestInfo;

    @Before
    public void setup() {
        mockClient = mock(SphereClient.class);
        mockCart = mock(Cart.class);
        mockCustomer = mock(Customer.class);
        mockRequestInfo = mock(HttpRequestInfo.class);

        when(mockClient.toString()).thenReturn(CLIENT_TO_STRING);
        when(mockCart.toString()).thenReturn(CART_TO_STRING);
        when(mockCustomer.toString()).thenReturn(CUSTOMER_TO_STRING);
        when(mockRequestInfo.toString()).thenReturn(REQUEST_TO_STRING);
    }

    @Test
    public void of() {
        CreatePaymentData cpd = CreatePaymentDataBuilder.of(mockClient, mockCart).build();
        assertThat(cpd.getSphereClient().toString()).isEqualTo(CLIENT_TO_STRING);
        assertThat(cpd.getCart().toString()).isEqualTo(CART_TO_STRING);
    }

    @Test
    public void addCustomer() {
        CreatePaymentData cpd = CreatePaymentDataBuilder.of(mockClient, mockCart).withCustomer(mockCustomer).build();

        assertThat(cpd.getCustomer().isPresent()).isTrue();
        assertThat(cpd.getCustomer().get().toString()).isEqualTo(CUSTOMER_TO_STRING);
    }

    @Test
    public void addHttpRequestInformation() {
        CreatePaymentData cpd = CreatePaymentDataBuilder.of(mockClient, mockCart).withHttpRequestInfo(mockRequestInfo).build();

        assertThat(cpd.getHttpRequestInfo().isPresent()).isTrue();
        assertThat(cpd.getHttpRequestInfo().get().toString()).isEqualTo(REQUEST_TO_STRING);
    }
}