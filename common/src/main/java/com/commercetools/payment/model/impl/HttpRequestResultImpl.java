package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.HttpRequestResult;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 8/9/16.
 */
public class HttpRequestResultImpl implements HttpRequestResult {
    private HttpRequest request;
    @Nullable
    private HttpResponse response;
    @Nullable
    private Throwable throwable;

    public HttpRequestResultImpl(HttpRequest request, HttpResponse response, Throwable throwable) {

        this.request = request;
        this.response = response;
        this.throwable = throwable;
    }

    @Override
    public HttpRequest getRequest() {
        return this.request;
    }

    @Override
    public Optional<HttpResponse> getResponse() {

        return Optional.ofNullable(this.response);
    }

    @Override
    public Optional<Throwable> getException() {

        return Optional.ofNullable(this.throwable);
    }
}
