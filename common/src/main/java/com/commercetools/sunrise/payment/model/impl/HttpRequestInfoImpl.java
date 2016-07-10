package com.commercetools.sunrise.payment.model.impl;

import com.commercetools.sunrise.payment.model.HttpRequestInfo;

import javax.annotation.Nullable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by mgatz on 7/20/16.
 */
public class HttpRequestInfoImpl implements HttpRequestInfo {
    @Nullable
    private Locale locale;
    @Nullable
    private Inet4Address ip4;
    @Nullable
    private Inet6Address ip6;

    @Override
    public Optional<Locale> getRequestLocale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public Optional<Inet4Address> getIPv4Address() {
        return Optional.ofNullable(ip4);
    }

    @Override
    public Optional<Inet6Address> getIPv6Address() {
        return Optional.ofNullable(ip6);
    }

    @Override
    public HttpRequestInfo requestLocale(Locale l) {
        this.locale = l;
        return this;
    }

    @Override
    public HttpRequestInfo ipv4Address(Inet4Address ipv4) {
        this.ip4 = ipv4;
        return this;
    }

    @Override
    public HttpRequestInfo ipv6Address(Inet6Address ipv6) {
        this.ip6 = ipv6;
        return this;
    }
}
