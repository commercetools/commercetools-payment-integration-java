package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/20/16.
 */
public class PaymentTransactionCreationResultBuilderTest {
    @Test
    public void setPaymentOnErrorResults() {
        Payment payment = Mockito.mock(Payment.class);
        String message = "A error message";
        PaymentTransactionCreationResult result = PaymentTransactionCreationResultBuilder.ofError(message, null, payment);
        assertThat(result.getMessage().get()).isEqualTo(message);
        assertThat(result.getOperationResult()).isEqualTo(OperationResult.FAILED);
        assertThat(result.getRelatedPaymentObject().get()).isEqualTo(payment);

    }

    @Test
    public void setNotPaymentOnErrorResults() {
        String message = "A error message";
        PaymentTransactionCreationResult result = PaymentTransactionCreationResultBuilder.ofError(message);
        assertThat(result.getMessage().get()).isEqualTo(message);
        assertThat(result.getOperationResult()).isEqualTo(OperationResult.FAILED);
        assertThat(result.getRelatedPaymentObject().isPresent()).isEqualTo(false);

    }
}