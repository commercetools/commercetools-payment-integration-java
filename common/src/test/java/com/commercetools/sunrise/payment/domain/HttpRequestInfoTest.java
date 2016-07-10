package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.model.HttpRequestInfo;
import org.junit.Before;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by mgatz on 7/20/16.
 */
public class HttpRequestInfoTest {
    private Inet4Address ip4Address;
    private Inet6Address ip6Address;

    private Locale locale;

    @Before
    public void setup() {
        locale = Locale.GERMANY;


    }

    @Test
    public void of() {
        HttpRequestInfo info = HttpRequestInfo.of();

        assertNotNull(info);
    }

    @Test
    public void addLocale() {
        HttpRequestInfo info = HttpRequestInfo.of();

        assertFalse(info.getRequestLocale().isPresent());

        info = info.requestLocale(locale);

        assertTrue(info.getRequestLocale().isPresent());
        assertEquals(Locale.GERMANY, info.getRequestLocale().get());
    }

    @Test
    public void addIpAddresses() {
        HttpRequestInfo info = HttpRequestInfo.of();

    }
}