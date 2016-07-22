package com.commercetools.sunrise.payment.domain;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        CreatePaymentData cpd = CreatePaymentData.of(mockClient, mockCart);
        assertEquals(CLIENT_TO_STRING, cpd.getSphereClient().toString());
        assertEquals(CART_TO_STRING, cpd.getCart().toString());
    }

    @Test
    public void addCustomer() {
        CreatePaymentData cpd = CreatePaymentData.of(mockClient, mockCart).withCustomer(mockCustomer);

        assertTrue(cpd.getCustomer().isPresent());
        assertEquals(CUSTOMER_TO_STRING, cpd.getCustomer().get().toString());
    }

    @Test
    public void addHttpRequestInformation() {
        CreatePaymentData cpd = CreatePaymentData.of(mockClient, mockCart).withHttpRequestInfo(mockRequestInfo);

        assertTrue(cpd.getHttpRequestInfo().isPresent());
        assertEquals(REQUEST_TO_STRING, cpd.getHttpRequestInfo().get().toString());
    }
}