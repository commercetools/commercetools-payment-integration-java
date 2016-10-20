package com.commercetools.sunrise.payment.methods;

import com.commercetools.sunrise.payment.model.CreatePaymentData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.carts.commands.updateactions.RemovePayment;
import io.sphere.sdk.commands.Command;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by mgatz on 7/10/16.
 */
public abstract class CreatePaymentMethodBase implements CreatePaymentMethod {

    /**
     * Method to remove all payments from the cart and then attaching a new created one.
     * @param cpd contains the data for the new payment
     * @return the newly created Payment object
     */
    protected CompletionStage<Payment> removePaymentsAndCreateNew(final CreatePaymentData cpd) {
        final Cart cart = cpd.getCart();
        final List<UpdateAction<Cart>> changeCartPaymentInfo = new ArrayList<>();


        if (cart.getPaymentInfo() != null) {
            changeCartPaymentInfo.addAll(cart.getPaymentInfo().getPayments().stream()
                    .map(RemovePayment::of)
                    .collect(Collectors.toList()));
        }

        final Command<Payment> createPaymentCommand = PaymentCreateCommand.of(createPaymentDraft(cpd).build());
        return cpd.getSphereClient().execute(createPaymentCommand)
                .thenCompose(p -> {
                    changeCartPaymentInfo.add(AddPayment.of(p));
                    return cpd.getSphereClient().execute(CartUpdateCommand.of(cpd.getCart(), changeCartPaymentInfo))
                            .thenApplyAsync(c -> p);
                });
    }

    /**
     * Creates a payment draft object used to create a new payment object at the CTP.
     * @param cpd the data object
     * @return the draft object
     */
    protected PaymentDraftBuilder createPaymentDraft(CreatePaymentData cpd) {

        PaymentDraftBuilder builder = PaymentDraftBuilder
                .of(cpd.getCart().getTotalPrice());

        if(cpd.getCustomer().isPresent()) {
            builder.customer(cpd.getCustomer().get());
        }

        return builder
                .paymentMethodInfo(cpd.getPaymentMethodinInfo());
    }

    protected static String getLanguageFromCartOrFallback(Cart cart) {
        if(null != cart.getLocale()) {
            return cart.getLocale().getLanguage();
        }

        return Locale.ENGLISH.getLanguage();
    }
}
