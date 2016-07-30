package com.commercetools.sunrise.payment.methods;

import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionDraft;
import io.sphere.sdk.payments.TransactionDraftBuilder;
import io.sphere.sdk.payments.TransactionType;
import io.sphere.sdk.payments.commands.PaymentUpdateCommand;
import io.sphere.sdk.payments.commands.updateactions.AddTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/27/16.
 */
public abstract class CreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    /**
     * Create a new payment transaction using the passed creation data.
     * @param data the data wrapper object
     * @param defaultType the default {@link TransactionType} that will be used if the data wrapper does not provide a special value
     * @return the completion stage that will return the created payment object
     */
    protected CompletionStage<Payment> createPaymentTransaction(CreatePaymentTransactionData data, TransactionType defaultType) {
        TransactionDraft transactionDraft = TransactionDraftBuilder.of(
                data.getTransactionType().orElse(defaultType),
                data.getPayment().getAmountPlanned()).build();
        List<UpdateAction<Payment>> commands = new ArrayList<>();
        commands.add(AddTransaction.of(transactionDraft));

        commands.addAll(addPaymentUpdateActions(data));

        return data.getSphereClient().execute(PaymentUpdateCommand.of(data.getPayment(), commands));
    }

    protected List<UpdateAction<Payment>> addPaymentUpdateActions(CreatePaymentTransactionData data) {
        return new ArrayList<>(); // returning a real instance allows overriding methods to reuse this with "super"
    }
}
