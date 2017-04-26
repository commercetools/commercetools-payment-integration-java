package com.commercetools.payment.utils.impl;

import com.commercetools.payment.model.CreatePaymentTransactionData;
import com.commercetools.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.queries.PaymentByIdGet;
import io.sphere.sdk.payments.queries.PaymentQuery;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Created by mgatz on 7/21/16.
 */
public class PaymentLookupHelperImpl implements PaymentLookupHelper {

    private final SphereClient sphereClient;

    public PaymentLookupHelperImpl(SphereClient client) {
        this.sphereClient = client;
    }

    @Override
    public CompletionStage<Payment> findPayment(String id) {
        PaymentByIdGet q = PaymentByIdGet.of(id);
        return sphereClient.execute(q);
    }

    @Override
    public CompletionStage<Optional<Payment>> findPayment(Cart cart, String pspId, String methodId) {
        PaymentQuery queryAllRefPayments = createPaymentsForCartQuery(cart, pspId, methodId);

        return sphereClient.execute(queryAllRefPayments)
                .thenApply(page -> page.getTotal() > 0 ? Optional.of(page.getResults().get(0)) : Optional.empty());
    }

    @Override
    public CompletionStage<Optional<Payment>> findPaymentWithoutTransaction(Cart cart, String pspId, String methodId) {
        if(null == cart.getPaymentInfo() || null == cart.getPaymentInfo().getPayments() || cart.getPaymentInfo().getPayments().isEmpty()) {
            return CompletableFuture.supplyAsync(() -> Optional.empty());
        }


        PaymentQuery queryAllRefPayments = createPaymentsForCartQuery(cart, pspId, methodId);

        queryAllRefPayments = queryAllRefPayments
                .plusPredicates(p -> p.transactions().isEmpty());

        return sphereClient.execute(queryAllRefPayments)
                .thenApply(page -> page.getTotal() > 0 ? Optional.of(page.getResults().get(0)) : Optional.empty());
    }

    @Override
    public CompletionStage<CreatePaymentTransactionData> findPaymentFor(CreatePaymentTransactionData data) {
        // I know changing parameters sucks a lot but with the small amount of time I have this solution MUST be perfectly fine for now
        return findPayment(data.getPaymentRef())
                .thenApply(p -> {
                    data.setPayment(p);
                    return data;
                });
    }

    private PaymentQuery createPaymentsForCartQuery(Cart cart, String pspId, String methodId) {
        PaymentQuery queryAllRefPayments = PaymentQuery.of()
                .plusPredicates(p -> p.paymentMethodInfo().paymentInterface().is(pspId))
                .plusPredicates(p -> p.paymentMethodInfo().method().is(methodId))
                .plusSort(p -> p.createdAt().sort().desc());
        if(null != cart.getPaymentInfo()) {
            for (Reference<Payment> ref : cart.getPaymentInfo().getPayments()) {
                queryAllRefPayments = queryAllRefPayments.plusPredicates(p -> p.id().is(ref.getId()));
            }
        }
        return queryAllRefPayments;
    }
}
