package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.types.CustomFields;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Inherited classes must be run with {@code @RunWith(MockitoJUnitRunner.class)}
 */
public class BasePayoneCreatePaymentTransactionMethodProviderTest {

    protected PayoneCreatePaymentTransactionMethodBase transactionMethod;

    @Mock
    protected Payment payment;

    @Mock
    protected CustomFields customFields;


    /**
     * Stub {@link #customFields} into {@link Payment#getCustom()}.
     */
    protected void applyStubbing() {
        when(payment.getCustom()).thenReturn(customFields);
    }

    public void handleSuccessfulServiceCall_default() throws Exception {
        PaymentTransactionCreationResult ptcr = transactionMethod.handleSuccessfulServiceCall(payment);
        assertThat(ptcr).isNotNull();
        assertThat(ptcr.getRelatedPaymentObject()).contains(payment);
        assertThat(ptcr.getException()).isNotPresent();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);

        assertThat(ptcr.getHandlingTask()).isNotNull();
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(ShopAction.CONTINUE);
        assertThat(ptcr.getHandlingTask().getRedirectUrl()).isNotPresent();

        assertThat(ptcr.getMessage()).isNotPresent();
    }
}
