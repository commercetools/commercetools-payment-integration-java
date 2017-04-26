package com.commercetools.payment;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CANCEL_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CREDIT_CARD_CARD_DATA_PLACEHOLDER;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CREDIT_CARD_FORCE_3D_SECURE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.CREDIT_CARD_TRUNCATED_CARD_NUMBER;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.ERROR_URL;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.SUCCESS_URL;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayoneCreditCardTest extends BasePayoneTest {

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

        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(client, "PAYONE", "CREDIT_CARD", cart, Long.toString(System.nanoTime()))
                                .configValue(CREDIT_CARD_FORCE_3D_SECURE, "true")
                                .configValue(SUCCESS_URL, "http://google.de")
                                .configValue(ERROR_URL, "http://google.de")
                                .configValue(CANCEL_URL, "http://google.de")
                                .configValue(CREDIT_CARD_CARD_DATA_PLACEHOLDER, "placeholder")
                                .configValue(CREDIT_CARD_TRUNCATED_CARD_NUMBER, "truncated")
                                .build())
                .toCompletableFuture().get();

        assertPaymentCreation(paymentCreationResult);

        // payment transaction creation is difficult to integration test cause the client side request
        // that provides lots of credit card data is not mockable
    }

    private void assertPaymentCreation(PaymentCreationResult paymentCreationResult) {
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getCustom().getFieldAsBoolean(CREDIT_CARD_FORCE_3D_SECURE)).isTrue();
    }
}
