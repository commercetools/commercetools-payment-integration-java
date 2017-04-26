package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.HttpRequestInfo;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.util.Locale;
import java.util.Optional;

/**
 * Created by mgatz on 7/20/16.
 */
public class HttpRequestInfoImpl implements HttpRequestInfo {
    @Nullable
    private Locale locale;
    @Nullable
    private InetAddress ip;
    @Nullable

    public HttpRequestInfoImpl(@Nullable Locale requestLocale,@Nullable InetAddress ipAddress) {
        this.locale = requestLocale;
        this.ip = ipAddress;
    }

    @Override
    public Optional<Locale> getRequestLocale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public Optional<InetAddress> getInetAddress() {
        return Optional.ofNullable(ip);
    }
}
