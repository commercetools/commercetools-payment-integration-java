package com.commercetools.sunrise.payment.payone.utils.impl;

import com.commercetools.sunrise.payment.payone.utils.PayoneHandlePaymentHelper;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpMethod;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpStatusCode;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.queries.PaymentByIdGet;

import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/26/16.
 */
public class PayoneHandlePaymentHelperImpl implements PayoneHandlePaymentHelper {

    private final SphereClient sphereClient;

    public PayoneHandlePaymentHelperImpl(SphereClient client) {
        this.sphereClient = client;
    }

    @Override
    public CompletionStage<Payment> requestHandling(String paymentId) {
        try (final HttpClient httpClient = SphereClientFactory.of().createHttpClient()) {
            return httpClient.execute(HttpRequest.of(HttpMethod.GET, buildUrl(paymentId)))
                    .thenCompose(response -> {
                        if(response.getStatusCode().equals(HttpStatusCode.OK_200)) {
                            return sphereClient.execute(PaymentByIdGet.of(paymentId));
                        }
                        return null;
                    });
        }
    }

    private String buildUrl(String paymentId) {
        return null; // use property provider here to get the base url
    }
}
