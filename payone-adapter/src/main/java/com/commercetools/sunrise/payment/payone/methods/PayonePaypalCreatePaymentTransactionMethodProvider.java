package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.domain.PaymentTransactionCreationResultBuilder;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethod;
import com.commercetools.sunrise.payment.methods.CreatePaymentTransactionMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentTransactionData;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.payone.config.PayoneConfigurationProvider;
import com.commercetools.sunrise.payment.utils.PaymentConnectorHelper;
import com.commercetools.sunrise.payment.utils.PaymentLookupHelper;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.http.HttpStatusCode;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionType;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;

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
                        HttpResponse response = PaymentConnectorHelper.of().sendHttpGetRequest(
                                buildHandleUrl(payment),
                                System.getenv(HTTP_BASIC_AUTH_USER),
                                System.getenv(HTTP_BASIC_AUTH_PASS));
                        if(response.getStatusCode() != HttpStatusCode.OK_200) {
                            return CompletableFuture.supplyAsync(() ->
                                    PaymentTransactionCreationResultBuilder.ofError(
                                            "Payone handle call (URL: "
                                                    + response.getAssociatedRequest().getUrl()
                                                    + ") returned wrong HTTP status: "
                                                    + response.getStatusCode()
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
