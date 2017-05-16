package com.commercetools.payment.utils.impl;

import com.commercetools.payment.model.HttpRequestResult;
import com.commercetools.payment.utils.PaymentConnectorHelper;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentConnectorHelperImpl implements PaymentConnectorHelper {

    @Override
    @Nonnull
    public CompletionStage<HttpRequestResult> sendHttpGetRequest(String url) {
        HttpRequest request = HttpRequest.of(HttpMethod.GET, url);

        // TODO: must be injected, instead of initialization!!!
        HttpClient client = SphereClientFactory.of().createHttpClient();

        return client.execute(request)
                     .thenApplyAsync(response -> HttpRequestResult.of(request, response, null))
                     .exceptionally(throwable -> HttpRequestResult.of(request, null, throwable))
                     .whenCompleteAsync((response, throwable) -> client.close());
    }
}
