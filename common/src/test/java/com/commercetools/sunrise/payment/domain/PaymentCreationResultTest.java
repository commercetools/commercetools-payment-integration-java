package com.commercetools.sunrise.payment.domain;

import com.commercetools.sunrise.payment.actions.HandlingTask;
import com.commercetools.sunrise.payment.actions.OperationResult;
import com.commercetools.sunrise.payment.actions.ShopAction;
import io.sphere.sdk.payments.Payment;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by mgatz on 7/20/16.
 */
public class PaymentCreationResultTest {
    @Test
    public void getOperationResult() {
        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.SUCCESS).build();
        assertEquals(OperationResult.SUCCESS, pcr.getOperationResult());
    }

    @Test
    public void getCreatedPaymentObject() {
        Payment pMock = mock(Payment.class);
        when(pMock.getExternalId()).thenReturn("foo"); // just to check that the returned payment object is the same

        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.SUCCESS).payment(pMock).build();
        assertTrue(pcr.getCreatedPaymentObject().isPresent());
        assertEquals("foo", pcr.getCreatedPaymentObject().get().getExternalId());
    }

    @Test
    public void hasCancelledPayments() {
        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.SUCCESS).hasCancelledPayments(true).build();

        assertTrue(pcr.hasCancelledPayments());
    }

    @Test
    public void getHandlingTask() {
        HandlingTask ht = mock(HandlingTask.class);
        when(ht.getAction()).thenReturn(ShopAction.CUSTOM);

        PaymentCreationResult pcr = PaymentCreationResultBuilder.of(OperationResult.FAILED).handlingTask(ht).build();
        assertThat(pcr.getHandlingTask().getAction(), is(ShopAction.CUSTOM));
    }
}