package com.commercetools.payment.domain;

import com.commercetools.payment.model.HttpRequestInfo;
import org.junit.Before;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/20/16.
 */
public class HttpRequestInfoTest {

    private Locale locale;
    private InetAddress inetAddress;

    @Before
    public void setup() throws UnknownHostException {
        locale = Locale.GERMANY;

        inetAddress = Inet4Address.getLocalHost();
    }

    @Test
    public void of() {
        HttpRequestInfo info = HttpRequestInfoBuilder.of().build();

        assertThat(info).isNotNull();
    }

    @Test
    public void addLocale() {
        HttpRequestInfo info = HttpRequestInfoBuilder.of().build();

        assertThat(info.getRequestLocale().isPresent()).isFalse();

        info = HttpRequestInfoBuilder.of().requestLocale(locale).build();

        assertThat(info.getRequestLocale().isPresent()).isTrue();
        assertThat(info.getRequestLocale().get()).isEqualTo(Locale.GERMANY);
    }

    @Test
    public void addIpAddresses() {
        HttpRequestInfo info = HttpRequestInfoBuilder.of().build();

        assertThat(info.getInetAddress().isPresent()).isFalse();

        info = HttpRequestInfoBuilder.of().inetAddress(inetAddress).build();

        assertThat(info.getInetAddress().get()).isEqualTo(inetAddress);
    }
}