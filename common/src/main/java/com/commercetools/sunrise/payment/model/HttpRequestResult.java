package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.model.impl.HttpRequestResultImpl;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import java.util.Optional;

/**
 * Created by mgatz on 8/9/16.
 */
public interface HttpRequestResult {

    static HttpRequestResult of(HttpRequest request, HttpResponse response, Throwable throwable) {
        return new HttpRequestResultImpl(request, response, throwable);
    }

    HttpRequest getRequest();
    Optional<HttpResponse> getResponse();
    Optional<Throwable> getException();
}
