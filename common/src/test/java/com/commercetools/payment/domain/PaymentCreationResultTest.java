package com.commercetools.payment.domain;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentCreationResult;
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
        assertThat(pcr.getRelatedPaymentObject().isPresent()).isTrue();
        assertThat(pcr.getRelatedPaymentObject().get().getExternalId()).isEqualTo("foo");
    }

    @Test
    public void getHandlingTask() {
        HandlingTask ht = mock(HandlingTask.class);
        when(ht.getAction()).thenReturn(ShopAction.CUSTOM);

        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.FAILED).handlingTask(ht).build();
        assertThat(pcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CUSTOM);
    }
}