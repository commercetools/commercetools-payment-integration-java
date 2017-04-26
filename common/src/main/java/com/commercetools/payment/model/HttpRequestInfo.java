package com.commercetools.payment.model;

import java.net.InetAddress;
import java.util.Locale;
import java.util.Optional;

/**
 * Data wrapper for HTTP client request data that could be used for payment purposes.
 * Created by mgatz on 7/20/16.
 */
public interface HttpRequestInfo {
    /**
     * @return the requests locale information
     */
    Optional<Locale> getRequestLocale();

    /**
     * @return the origin IP address of the request
     */
    Optional<InetAddress> getInetAddress();
}
