package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PayoneCreditCardCreatePaymentTransactionMethodProviderTest
        extends BasePayoneRedirectTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        transactionMethod = PayoneCreditCardCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    @Override
    public void handleSuccessfulServiceCall_withRedirectUrl() throws Exception {
        super.handleSuccessfulServiceCall_withRedirectUrl();
    }

    /**
     * Opposite to other redirect-bases payment methods, Credit card might have payments without redirect
     * (without 3DS verification).
     */
    @Test
    public void handleSuccessfulServiceCall_success_withoutRedirectUrl() throws Exception {
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