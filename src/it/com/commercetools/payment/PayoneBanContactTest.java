package com.commercetools.payment;

import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import com.neovisionaries.i18n.CountryCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.commercetools.config.ItConfig.getPayoneIntegrationUrl;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.BANK_GROUP_TYPE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CANCEL_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.ERROR_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.HANDLE_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.SUCCESS_URL;
import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.BANK_TRANSFER_BANCONTACT;
import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.BANK_TRANSFER_IDEAL;
import static io.sphere.sdk.payments.TransactionState.PENDING;
import static io.sphere.sdk.payments.TransactionType.CHARGE;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayoneBanContactTest extends BasePayoneTest {

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(1, CountryCode.BE);// payment is only available in Belgium
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
    }

    @Test
    public void createPaymentCreationMethod() throws ExecutionException, InterruptedException {

        String reference = generateTestPayoneReference("bancontact-test");
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(CreatePaymentDataBuilder.of(
                        client,
                        "PAYONE",
                        BANK_TRANSFER_BANCONTACT,
                        cart, reference)
                        .configValue(SUCCESS_URL, "http://google.de")
                        .configValue(ERROR_URL, "http://google.de")
                        .configValue(CANCEL_URL, "http://google.de")
                        .build())
                .toCompletableFuture().get();

        assertPaymentObjectCreation(paymentCreationResult, reference);

        // user clicked "buy now" -> create transaction, trigger handle payment, return updated payment object
        PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl())
                                .build())
                .toCompletableFuture().get();

        assertPaymentTransactionObjectCreation(paymentTransactionCreationResult, CHARGE, PENDING);
    }

}
