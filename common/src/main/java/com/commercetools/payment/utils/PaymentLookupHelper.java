package com.commercetools.payment.utils;

import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.utils.impl.PaymentLookupHelperImpl;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.payments.Payment;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/21/16.
 */
public interface PaymentLookupHelper {
    public static PaymentLookupHelper of(SphereClient client) {
        return new PaymentLookupHelperImpl(client);
    }

    /**
     * Find a payment object using its unique ID.
     *
     * @param id the unique ID of the payment object
     * @return the payment object if one exists
     */
    CompletionStage<Payment> findPayment(String id);

    /**
     * Find a payment object related to the passed cart using its payment service provider ID and its method ID.
     * If more then one object is found then the newest one (concidering the creationdate) will be returned only.
     *
     * @param cart the cart object to lookup the payment objects attached to
     * @param pspId the payment service provider ID (mapped to "interfaceId" at CTP)
     * @param methodId the payment method ID (mapped to "paymentMethodInfo.paymentInterface" at CTP)
     * @return the payment object if one exists
     */
    CompletionStage<Optional<Payment>> findPayment(Cart cart, String pspId, String methodId);

    /**
     * Find a payment object related to the passed cart using its payment service provider ID and its method ID that has no transaction attached.
     * If more then on object is found then the newest one (concidering the creationdate) will be returned only.
     *
     * @param cart a cart for which a new payment must be created.
     * @param pspId the payment service provider ID (mapped to "interfaceId" at CTP)
     * @param methodId the payment method ID (mapped to "paymentMethodInfo.paymentInterface" at CTP)
     * @return {@link CompletionStage} of optional {@link Payment} if found any.
     */
    CompletionStage<Optional<Payment>> findPaymentWithoutTransaction(Cart cart, String pspId, String methodId);

    /**
     * Find a payment object for the provided data.
     * @param data the data holding object
     * @return the parameter enriched with the payment object
     */
    CompletionStage<CreatePaymentTransactionData> findPaymentFor(CreatePaymentTransactionData data);
}
