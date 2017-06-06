package com.commercetools.payment;

import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.CREDIT_CARD;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
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

        String reference = generateTestPayoneReference("cc-test");
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(client, "PAYONE", CREDIT_CARD, cart, reference)
                                .configValue(CREDIT_CARD_FORCE_3D_SECURE, "true")
                                .configValue(SUCCESS_URL, "http://google.de")
                                .configValue(ERROR_URL, "http://google.de")
                                .configValue(CANCEL_URL, "http://google.de")
                                .configValue(CREDIT_CARD_CARD_DATA_PLACEHOLDER, "placeholder")
                                .configValue(CREDIT_CARD_TRUNCATED_CARD_NUMBER, "truncated")
                                .build())
                .toCompletableFuture().get();

        assertPaymentCreation(paymentCreationResult, reference);

        // payment transaction creation is difficult to integration test cause the client side request
        // that provides lots of credit card data is not mockable
    }

    private void assertPaymentCreation(PaymentCreationResult paymentCreationResult, String reference) {
        assertPaymentObjectCreation(paymentCreationResult, reference);
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getCustom().getFieldAsBoolean(CREDIT_CARD_FORCE_3D_SECURE)).isTrue();
    }
}
