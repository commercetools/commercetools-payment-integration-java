package com.commercetools.sunrise.payment.utils;

import com.commercetools.sunrise.payment.utils.impl.PaymentConnectorHelperImpl;
import io.sphere.sdk.http.HttpResponse;

/**
 * Created by mgatz on 7/28/16.
 */
public interface PaymentConnectorHelper {

    static PaymentConnectorHelper of() {
        return new PaymentConnectorHelperImpl();
    }

    /**
     * Create a HTTP client and execute a GET request.
     * @param url the URL to be requested
     * @return the HTTP response
     */
    HttpResponse sendHttpGetRequest(String url);

    HttpResponse sendHttpGetRequest(String url, String userName, String password);
}
