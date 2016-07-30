package com.commercetools.sunrise.payment.methods;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.commands.Command;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;
import io.sphere.sdk.payments.commands.PaymentUpdateCommand;
import io.sphere.sdk.payments.commands.updateactions.ChangeAmountPlanned;
import io.sphere.sdk.payments.commands.updateactions.SetMethodInfoMethod;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/10/16.
 */
public abstract class CreatePaymentMethodBase implements CreatePaymentMethod {

    /**
     * Creates a function that checks for an existing payment without transactions attached to the provided cart (within data object)
     * and then creates or updates a payment object depending on the result of the check.
     * @return
     */
    protected CompletionStage<Payment> createOrUpdatePayment(CreatePaymentData cpd) {
            // check payment without transaction for this method exists
        return PaymentLookupHelper.of(cpd.getSphereClient())
                .findPaymentWithoutTransaction(cpd.getCart(), cpd.getPaymentMethodinInfo().getPaymentInterface(), cpd.getPaymentMethodinInfo().getMethod())
                .thenCompose(payment -> {
                    Command<Payment> createOrUpdateCommand = null;
                    if(!payment.isPresent()) {
                        // create payment at CTP
                        createOrUpdateCommand = PaymentCreateCommand.of(createPaymentDraft(cpd).build());
                        return cpd.getSphereClient().execute(createOrUpdateCommand)
                                .thenCompose(p -> cpd.getSphereClient().execute(CartUpdateCommand.of(cpd.getCart(), AddPayment.of(p))).thenApplyAsync(c -> p));
                    }

                    // update an existing payment objects payment method as it currently has no transaction
                    createOrUpdateCommand = createPaymentMethodUpdateCommand(payment.get(), cpd);
                    return cpd.getSphereClient().execute(createOrUpdateCommand);

                });
    }

    /**
     * Creates a payment draft object used to create a new payment object at the CTP.
     * @param cpd the data object
     * @return the draft object
     */
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        PaymentDraftBuilder builder = PaymentDraftBuilder
                .of(cpd.getCart().getTotalPrice())
                .custom(CustomFieldsDraftBuilder.ofTypeKey("payment-WALLET")
                        .addObject("reference", cpd.getReference())
                        .addObject("languageCode", cpd.getCart().getLocale().getLanguage())
                        .build());

        if(cpd.getCustomer().isPresent()) {
            builder.customer(cpd.getCustomer().get());
        }

        return builder
                .paymentMethodInfo(cpd.getPaymentMethodinInfo());
    }

    /**
     * Creates a payment update command object that allows updating the payment method information of an existing
     * payment object.
     * @param payment the payment object to be updated
     * @param cpd the data object
     * @return the update command
     */
    protected PaymentUpdateCommand createPaymentMethodUpdateCommand(Payment payment, CreatePaymentData cpd) {
        PaymentMethodInfo methodInfo = cpd.getPaymentMethodinInfo();
        List<UpdateAction<Payment>> updateCommands = new ArrayList<>();

        updateCommands.add(SetMethodInfoMethod.of(methodInfo.getMethod()));
        updateCommands.add(ChangeAmountPlanned.of(cpd.getCart().getTotalPrice()));

        return PaymentUpdateCommand.of(payment, updateCommands);
    }
}
