package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import org.junit.Test;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REDIRECT_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test transactions which optionally could have redirect action
 */
public class BasePayoneRedirectTransactionMethodProviderTest
        extends BasePayoneCreatePaymentTransactionMethodProviderTest {

    /**
     * With redirect most of the payments behave the same.
     */
    @Test
    public void handleSuccessfulServiceCall_withRedirectUrl() throws Exception {
        when(customFields.getFieldAsString(REDIRECT_URL)).thenReturn("http://woot.com");
        PaymentTransactionCreationResult ptcr = transactionMethod.handleSuccessfulServiceCall(payment);
        assertThat(ptcr).isNotNull();
        assertThat(ptcr.getRelatedPaymentObject()).contains(payment);
        assertThat(ptcr.getException()).isNotPresent();

        assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);

        assertThat(ptcr.getHandlingTask()).isNotNull();
        assertThat(ptcr.getHandlingTask().getAction()).isEqualTo(ShopAction.REDIRECT);
        assertThat(ptcr.getHandlingTask().getRedirectUrl()).contains("http://woot.com");
    }
}
