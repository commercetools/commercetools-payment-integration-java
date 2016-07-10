package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import com.commercetools.sunrise.payment.model.PaymentCreationResult;
import io.sphere.sdk.payments.Payment;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultTest {
    @Test
    public void getOperationResult() {
        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.SUCCESS).build();
        assertThat(pcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
    }

    @Test
    public void getCreatedPaymentObject() {
        Payment pMock = mock(Payment.class);
        when(pMock.getExternalId()).thenReturn("foo"); // just to check that the returned payment object is the same

        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.SUCCESS).payment(pMock).build();
        assertThat(pcr.getCreatedPaymentObject().isPresent()).isTrue();
        assertThat(pcr.getCreatedPaymentObject().get().getExternalId()).isEqualTo("foo");
    }

    @Test
    public void getHandlingTask() {
        HandlingTask ht = mock(HandlingTask.class);
        when(ht.getAction()).thenReturn(ShopAction.CUSTOM);

        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.FAILED).handlingTask(ht).build();
        assertThat(pcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CUSTOM);
    }
}