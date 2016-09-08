package com.commercetools.sunrise.payment.model;

import com.commercetools.sunrise.payment.actions.OperationResult;
import io.sphere.sdk.payments.Payment;

import java.util.Optional;

/**
 * Created by mgatz on 8/8/16.
 */
public interface PaymentOperationResult {
    /**
     * @return the basic operation result
     */
    OperationResult getOperationResult();

    /**
     * If the operation succeeded then this is the payment object that has been created.
     * @return the created payment object
     */
    Optional<Payment> getRelatedPaymentObject();

    /**
     * If the operation failed because of an exception, then it is stored here.
     * @return the exception
     */
    Optional<Throwable> getException();

    /**
     * Can provide a message that describes the cause of the operation result.
     * @return the message
     */
    Optional<String> getMessage();
}
