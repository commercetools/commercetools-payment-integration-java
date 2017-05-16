package com.commercetools.payment.model.impl;

import com.commercetools.payment.model.HttpRequestResult;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Created by mgatz on 8/9/16.
 */
public class HttpRequestResultImpl implements HttpRequestResult {
    @Nonnull
    private HttpRequest request;
    @Nullable
    private HttpResponse response;
    @Nullable
    private Throwable throwable;

    public HttpRequestResultImpl(@Nonnull HttpRequest request,
                                 @Nullable HttpResponse response,
                                 @Nullable Throwable throwable) {

        this.request = request;
        this.response = response;
        this.throwable = throwable;
    }

    @Override
    @Nonnull
    public HttpRequest getRequest() {
        return this.request;
    }

    @Override
    public Optional<HttpResponse> getResponse() {
        return ofNullable(this.response);
    }

    @Override
    public Optional<Throwable> getException() {
        return ofNullable(this.throwable);
    }
}
