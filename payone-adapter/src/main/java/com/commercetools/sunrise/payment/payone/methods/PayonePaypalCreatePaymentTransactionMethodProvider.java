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
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.TransactionType;
import io.sphere.sdk.payments.commands.updateactions.SetCustomField;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

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
            this.handleUrl = cptd.getConfigByName("PayoneHandleURL");
            return createPaymentTransaction(cptd, getDefaultTransactionType(cptd))
                    .thenCompose(payment -> {
                        // call PSP connector to handle the payment synchronously
                        HttpResponse response = PaymentConnectorHelper.of().sendHttpGetRequest(
                                buildHandleUrl(payment),
                                System.getenv("PAYONE_AUTH_USER"),
                                System.getenv("PAYONE_AUTH_PASS"));
                        // TODO: add handling of error response here
                        return PaymentLookupHelper.of(cptd.getSphereClient()).findPayment(payment.getId()) // update the payment object
                            .thenApply(updatedPayment -> {
                                String redirectURL = updatedPayment.getCustom().getFieldAsString("redirectUrl");
                                if (null != redirectURL) {
                                    return PaymentTransactionCreationResultBuilder.of(OperationResult.SUCCESS)
                                            .payment(payment)
                                            .handlingTask(HandlingTask.of(ShopAction.REDIRECT_AFTER_CHECKOUT).redirectUrl(redirectURL)).build();
                                }
                                return PaymentTransactionCreationResultBuilder.ofError();
                            });
                    });
        };
    }

    @Override
    protected List<UpdateAction<Payment>> addPaymentUpdateActions(CreatePaymentTransactionData data) {
        List<UpdateAction<Payment>> updateActions = super.addPaymentUpdateActions(data);

        updateActions.add(SetCustomField.ofObject("successUrl", data.getConfigByName("successUrl")));
        updateActions.add(SetCustomField.ofObject("errorUrl", data.getConfigByName("errorUrl")));
        updateActions.add(SetCustomField.ofObject("cancelUrl", data.getConfigByName("cancelUrl")));

        return updateActions;
    }

    private TransactionType getDefaultTransactionType(CreatePaymentTransactionData cptd) {
        return PayoneConfigurationProvider.of().load().getTransactionType(cptd.getPayment().getPaymentMethodInfo().getMethod());
    }

    private String buildHandleUrl(Payment p) {

        return this.handleUrl + p.getId();
    }
}
