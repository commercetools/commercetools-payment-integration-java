package com.commercetools.payment.model;

import com.commercetools.payment.actions.HandlingTask;

/**
 * Provides all information a payment create operation creates for the calling shop system.
 * Created by mgatz on 7/20/16.
 */
public interface PaymentCreationResult extends PaymentOperationResult {

    /**
     * Get the task that describes what actions the shop should do next.
     * @return the task description object
     */
    HandlingTask getHandlingTask();
}
