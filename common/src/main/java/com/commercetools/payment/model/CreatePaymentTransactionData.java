package com.commercetools.payment.model;

import com.commercetools.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionType;

import java.util.Optional;

/**
 * Provides a wrapper for all data that could possibly be needed by a {@link PaymentServiceProvider} implementation
 * to create a new payment transaction object.
 *
 * Created by mgatz on 7/27/16.
 */
public interface CreatePaymentTransactionData extends PaymentInteractionData {
    /**
     * @return the payment ref the transaction should be created for
     */
    String getPaymentRef();

    /**
     * @return get the payment object the transaction should be created for
     */
    Payment getPayment();

    /**
     * Set the payment object the transaction should be created for.
     * @param p the payment object
     */
    void setPayment(Payment p);

    /**
     * @return the type of the transaction to be created
     */
    Optional<TransactionType> getTransactionType();

    /**
     * Set the type of the transaction to be created. If non is set then the payment methods configured default will be used.
     * @param type
     */
    void setTransactionType(TransactionType type);
}
