package com.commercetools.payment.model;

import com.commercetools.payment.model.impl.HttpRequestResultImpl;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by mgatz on 8/9/16.
 */
public interface HttpRequestResult {

    static HttpRequestResult of(@Nonnull HttpRequest request,
                                @Nullable HttpResponse response,
                                @Nullable Throwable throwable) {
        return new HttpRequestResultImpl(request, response, throwable);
    }

    @Nonnull
    HttpRequest getRequest();
    Optional<HttpResponse> getResponse();
    Optional<Throwable> getException();
}
