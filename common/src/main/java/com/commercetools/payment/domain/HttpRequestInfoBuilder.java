package com.commercetools.payment.domain;

import com.commercetools.payment.model.HttpRequestInfo;
import com.commercetools.payment.model.impl.HttpRequestInfoImpl;

import java.net.InetAddress;
import java.util.Locale;

/**
 * Created by mgatz on 7/10/16.
 */
public class HttpRequestInfoBuilder {
    private Locale requestLocale;
    private InetAddress inetAddress;

    private HttpRequestInfoBuilder() { }

    /**
     * @return an instance of the builder
     */
    public static HttpRequestInfoBuilder of() {
        return new HttpRequestInfoBuilder();
    }

    /**
     * Add locale information of the HTTP request to the shop.
     * @param l
     * @return enriched self
     */
    public HttpRequestInfoBuilder requestLocale(Locale l) {
        this.requestLocale = l;
        return this;
    }

    /**
     * Add IPv4 address information of the client request.
     * @param ip
     * @return enriched self
     */
    public HttpRequestInfoBuilder inetAddress(InetAddress ip) {
        this.inetAddress = ip;
        return this;
    }

    public HttpRequestInfo build() {
        return new HttpRequestInfoImpl(requestLocale, inetAddress);
    }
}
