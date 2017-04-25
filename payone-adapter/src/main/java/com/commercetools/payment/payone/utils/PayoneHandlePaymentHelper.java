package com.commercetools.payment.payone.utils;

import com.commercetools.payment.payone.utils.impl.PayoneHandlePaymentHelperImpl;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.Payment;

import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/26/16.
 */
public interface PayoneHandlePaymentHelper {
    static PayoneHandlePaymentHelper of(SphereClient client) {
        return new PayoneHandlePaymentHelperImpl(client);
    }

    /**
     * Does a synchronous call to the PSP connector and forces the handling of the payment with the passed ID.
     * @param paymentId the payment ID
     * @return the updated payment object requested from the CTP
     */
    CompletionStage<Payment> requestHandling(String paymentId);
}
