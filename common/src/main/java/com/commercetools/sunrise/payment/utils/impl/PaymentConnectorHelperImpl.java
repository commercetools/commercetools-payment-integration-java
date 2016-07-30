package com.commercetools.sunrise.payment.utils.impl;

import com.commercetools.sunrise.payment.utils.PaymentConnectorHelper;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.*;
import org.apache.commons.codec.binary.Base64;

import java.util.concurrent.ExecutionException;

/**
 * Created by mgatz on 7/28/16.
 */
public class PaymentConnectorHelperImpl implements PaymentConnectorHelper {

    @Override
    public HttpResponse sendHttpGetRequest(String url) {
        try(HttpClient client = SphereClientFactory.of().createHttpClient()) {
            HttpRequest request = HttpRequest.of(HttpMethod.GET, url);

            return client.execute(request).toCompletableFuture().get();
        } catch (InterruptedException e) {
            // TODO: add logging
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO: add logging
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public HttpResponse sendHttpGetRequest(String url, String userName, String password) {
        String basicAuthKey = "Basic " + new String(Base64.encodeBase64((userName + ":" + password).getBytes()));

        try(HttpClient client = SphereClientFactory.of().createHttpClient()) {
            HttpRequest request = HttpRequest.of(HttpMethod.GET, url, HttpHeaders.of("Authorization", basicAuthKey), null);

            return client.execute(request).toCompletableFuture().get();
        } catch (InterruptedException e) {
            // TODO: add logging
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO: add logging
            e.printStackTrace();
        }

        return null;
    }
}
