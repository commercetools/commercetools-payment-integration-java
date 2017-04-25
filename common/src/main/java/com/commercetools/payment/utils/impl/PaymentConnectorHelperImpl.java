package com.commercetools.payment.utils.impl;

import com.commercetools.payment.model.HttpRequestResult;
import com.commercetools.payment.utils.PaymentConnectorHelper;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentConnectorHelperImpl implements PaymentConnectorHelper {

    @Override
    public HttpRequestResult sendHttpGetRequest(String url) {
        HttpRequest request = HttpRequest.of(HttpMethod.GET, url);

        try(HttpClient client = SphereClientFactory.of().createHttpClient()) {

            HttpResponse response = client.execute(request).toCompletableFuture().get(10000, TimeUnit.MILLISECONDS);
            return HttpRequestResult.of(request, response, null);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return HttpRequestResult.of(request, null, e);
        }
    }
}
