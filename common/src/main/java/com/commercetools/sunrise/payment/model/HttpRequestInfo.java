package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.model.impl.HttpRequestInfoImpl;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.Locale;
import java.util.Optional;

/**
 * Data wrapper for HTTP client request data that could be used for payment purposes.
 * Created by mgatz on 7/20/16.
 */
public interface HttpRequestInfo {

    static HttpRequestInfo of() {
        return new HttpRequestInfoImpl();
    }

    /**
     * @return the requests locale information
     */
    Optional<Locale> getRequestLocale();

    /**
     * @return the requests IPv4 address if present
     */
    Optional<Inet4Address> getIPv4Address();

    /**
     * @return the request IPv6 address if present
     */
    Optional<Inet6Address> getIPv6Address();

    /**
     * Add locale information of the HTTP request to the shop.
     * @param l
     * @return enriched self
     */
    public HttpRequestInfo requestLocale(Locale l);

    /**
     * Add IPv4 address information of the client request.
     * @param ipv4
     * @return enriched self
     */
    public HttpRequestInfo ipv4Address(Inet4Address ipv4);

    /**
     * Add IPv6 address information of the client request.
     * @param ipv6
     * @return enriched self
     */
    public HttpRequestInfo ipv6Address(Inet6Address ipv6);
}
