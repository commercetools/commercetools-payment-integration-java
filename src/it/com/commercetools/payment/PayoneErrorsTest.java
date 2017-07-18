package com.commercetools.payment;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.commercetools.config.ItConfig.getPayoneIntegrationUrl;
import static com.commercetools.payment.actions.ShopAction.HANDLE_ERROR;
import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.WALLET_PAYPAL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PayoneErrorsTest extends BasePayoneTest {

    private PaymentCreationResult paymentCreationResult;

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(1);

        String reference = generateTestPayoneReference("error-test");
        paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                WALLET_PAYPAL,
                                cart,
                                reference)
                                .configValue(SUCCESS_URL, "http://google.de")
                                .configValue(ERROR_URL, "http://google.de")
                                .configValue(CANCEL_URL, "http://google.de").build())
                .toCompletableFuture().get();

        assertPaymentObjectCreation(paymentCreationResult, reference);
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
    }

    @Test
    public void testCorrectPaymentAgainstWrongHandlingUrl() throws ExecutionException, InterruptedException {
        // user clicked "buy now" -> create transaction, trigger handle payment, but the payment URL is wrong
        PaymentTransactionCreationResult ptcr = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, "http://some-fake-url.wat/foo-bar/")
                                .build())
                .toCompletableFuture().get();

        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.FAILED);
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(HANDLE_ERROR);

        assertThat(ptcr.getRelatedPaymentObject()).isNotPresent();
        assertThat(ptcr.getException()).isPresent();
        assertThat(ptcr.getException().map(Throwable::getMessage).orElse(null)).contains("HttpException");

        assertThat(ptcr.getMessage()).isPresent();
        assertThat(ptcr.getMessage().orElse(null)).contains("some-fake-url.wat");
    }

    @Test
    public void testCorrectPaymentIdAgainst404() throws ExecutionException, InterruptedException {

        // use correct service reference, but just incorrect handling path

        PaymentTransactionCreationResult ptcr = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl() + "/some-fake-subpath/")
                                .build())
                .toCompletableFuture().get();

        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getRelatedPaymentObject()).isNotPresent();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.FAILED);
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(HANDLE_ERROR);

        assertThat(ptcr.getRelatedPaymentObject()).isNotPresent();

        // check the message contains "what is wrong" substrings
        assertThat(ptcr.getMessage()).isPresent();
        assertThat(ptcr.getMessage().orElse(null)).contains(paymentCreationResult.getRelatedPaymentObject().get().getId());
        assertThat(ptcr.getMessage().orElse(null)).contains("/some-fake-subpath/");
    }

    @Test
    public void testIncorrectPaymentIdAgainstCorrectHandlingUrl() throws ExecutionException, InterruptedException {
        // user clicked "buy now" -> create transaction, trigger handle payment, but the payment id is incorrect
        PaymentTransactionCreationResult ptcr = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, "some-fake-id")
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl())
                                .build())
                .toCompletableFuture().get();

        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.FAILED);
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(HANDLE_ERROR);

        assertThat(ptcr.getRelatedPaymentObject()).isNotPresent();

        assertThat(ptcr.getMessage()).isPresent();
        assertThat(ptcr.getMessage().orElse(null)).contains("some-fake-id");
    }

}
