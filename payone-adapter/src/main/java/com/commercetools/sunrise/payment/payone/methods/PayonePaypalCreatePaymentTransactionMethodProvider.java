package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.HttpRequestResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.payone.config.PayoneConfigurationProvider;
import com.commercetools.sunrise.payment.utils.PaymentConnectorHelper;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.http.HttpStatusCode;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.HANDLE_URL;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.REDIRECT_URL;

/**
 * Created by mgatz on 7/27/16.
 */
public class PayonePaypalCreatePaymentTransactionMethodProvider extends CreatePaymentTransactionMethodBase implements CreatePaymentTransactionMethod {

    private String handleUrl;
    private String paymentRef;

    private PayonePaypalCreatePaymentTransactionMethodProvider() {
    }

    public static CreatePaymentTransactionMethod of() {
        return new PayonePaypalCreatePaymentTransactionMethodProvider();
    }

    @Override
    public Function<CreatePaymentTransactionData, CompletionStage<PaymentTransactionCreationResult>> create() {

        return cptd -> {
            this.handleUrl = cptd.getConfigByName(HANDLE_URL);
            return createPaymentTransaction(cptd, getDefaultTransactionType(cptd))
                    .thenCompose(payment -> {
                        // call PSP connector to handle the payment synchronously
                        HttpRequestResult result = PaymentConnectorHelper.of().sendHttpGetRequest(
                                buildHandleUrl(payment));
                        if(!result.getResponse().isPresent()) {
                            return CompletableFuture.supplyAsync(() ->
                                    PaymentTransactionCreationResultBuilder.ofError(
                                            "Payone handle call (URL: "
                                                    + result.getRequest().getUrl()
                                                    + ") returned failed!",
                                                    result.getException().get()));
                        }
                        else if(result.getResponse().get().getStatusCode() != HttpStatusCode.OK_200) {
                            return CompletableFuture.supplyAsync(() ->
                                    PaymentTransactionCreationResultBuilder.ofError(
                                            "Payone handle call (URL: "
                                                    + result.getRequest().getUrl()
                                                    + ") returned wrong HTTP status: "
                                                    + result.getResponse().get().getStatusCode()
                                                    + "! Check Payone Connector log files!"));
                        }
                        return PaymentLookupHelper.of(cptd.getSphereClient()).findPayment(payment.getId()) // update the payment object
                            .thenApply(updatedPayment -> {
                                String redirectURL = updatedPayment.getCustom().getFieldAsString(REDIRECT_URL);
                                if (null != redirectURL) {
                                    return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                                            .payment(payment)
                                            .handlingTask(HandlingTask.of(ShopAction.REDIRECT_AFTER_CHECKOUT).redirectUrl(redirectURL)).build();
                                }
                                return PaymentTransactionCreationResultBuilder.ofError("There was no redirect set at the payment object ("
                                        + updatedPayment.getId()
                                        +"). Check Payone Connector log files!");
                            });
                    }).exceptionally(t -> {
                        return PaymentTransactionCreationResultBuilder.ofError("An exception occured that could not be handled: ", t);
                    });
        };
    }

    private TransactionType getDefaultTransactionType(CreatePaymentTransactionData cptd) {
        return PayoneConfigurationProvider.of().load().getTransactionType(cptd.getPayment().getPaymentMethodInfo().getMethod());
    }

    private String buildHandleUrl(Payment p) {

        return this.handleUrl + p.getId();
    }
}
