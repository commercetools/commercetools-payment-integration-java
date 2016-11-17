package com.commercetools.sunrise.payment;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import com.commercetools.sunrise.payment.service.PaymentAdapterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.*;
import static org.assertj.core.api.Assertions.assertThat;

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

        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(CreatePaymentDataBuilder.of(
                        client,
                        "PAYONE",
                        "BANK_TRANSFER-SOFORTUEBERWEISUNG",
                        cart, Long.toString(new Date().getTime()))
                        .configValue(SUCCESS_URL, "http://google.de")
                        .configValue(ERROR_URL, "http://google.de")
                        .configValue(CANCEL_URL, "http://google.de")
                        .build())
                .toCompletableFuture().get();
        assertThat(paymentCreationResult).isNotNull();
        assertThat(paymentCreationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
        assertThat(paymentCreationResult.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(paymentCreationResult.getRelatedPaymentObject().get().getAmountPlanned()).isEqualTo(cart.getTotalPrice());
    }

}
