package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import io.sphere.sdk.payments.Payment;

import java.util.Optional;

/**
 * Provides all information a payment create operation creates for the calling shop system.
 * Created by mgatz on 7/20/16.
 */
public interface PaymentCreationResult {
    /**
     * @return the basic operation result
     */
    OperationResult getOperationResult();

    /**
     * If the operation succeeded then this is the payment object that has been created.
     * @return the created payment object
     */
    Optional<Payment> getCreatedPaymentObject();

    /**
     * @return true if there were other payment objects at the CTP that were cancelled before creating this new payment object
     */
    boolean hasCancelledPayments();

    /**
     * Get the task that describes what actions the shop should do next.
     * @return the task description object
     */
    HandlingTask getHandlingTask();
}
