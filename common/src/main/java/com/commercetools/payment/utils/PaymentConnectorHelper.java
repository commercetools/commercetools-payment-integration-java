package com.commercetools.payment.utils;

import com.commercetools.payment.model.HttpRequestResult;
import com.commercetools.payment.utils.impl.PaymentConnectorHelperImpl;

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
    HttpRequestResult sendHttpGetRequest(String url);
}
