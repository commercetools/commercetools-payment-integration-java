package com.commercetools.payment;

import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;

/**
 * Created by mgatz on 7/10/16.
 */
public class PayoneSofortTest extends BasePayoneTest {

    @Before
    public void setup() throws ExecutionException, InterruptedException {
        super.setup(1);
    }

    @After
    public void tearDown() throws Exception {
        shutdown();
    }

    @Test
    public void createPaymentCreationMethod() throws ExecutionException, InterruptedException {

        String reference = generateTestPayoneReference("sofort-test");
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(CreatePaymentDataBuilder.of(
                        client,
                        "PAYONE",
                        "BANK_TRANSFER-SOFORTUEBERWEISUNG",
                        cart, reference)
                        .configValue(SUCCESS_URL, "http://google.de")
                        .configValue(ERROR_URL, "http://google.de")
                        .configValue(CANCEL_URL, "http://google.de")
                        .build())
                .toCompletableFuture().get();

        assertPaymentObjectCreation(paymentCreationResult, reference);
    }

}
