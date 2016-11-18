package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.config.ItConfig.getPayoneIntegrationUrl;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.HANDLE_URL;

/**
 *
 * @author mht@dotsource.de
 *
 */
public class PayonePrepaidTest extends BasePayoneTest {

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(2);
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
    }

    @Test
    public void testPaymentFlow() throws ExecutionException, InterruptedException {
        // user selected paypal
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                "BANK_TRANSFER-ADVANCE",
                                cart,
                                Long.toString(new Date().getTime())).build())
                .toCompletableFuture().get();

        assertPaymentObjectCreation(paymentCreationResult);

        // user clicked "buy now" -> create transaction, trigger handle payment, return updated payment object
        PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl())
                                .build())
                .toCompletableFuture().get();

        assertPaymentTransactionObjectCreation(paymentTransactionCreationResult);
    }

}
