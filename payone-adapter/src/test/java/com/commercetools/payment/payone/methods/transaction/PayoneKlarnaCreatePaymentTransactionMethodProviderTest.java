package com.commercetools.payment.payone.methods.transaction;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REDIRECT_URL;
import static com.commercetools.payment.payone.config.PayonePaymentMethodKeys.INVOICE_KLARNA;
import static io.sphere.sdk.payments.TransactionType.CHARGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayoneKlarnaCreatePaymentTransactionMethodProviderTest
        extends BasePayoneCreatePaymentTransactionMethodProviderTest {

    @Before
    public void setUp() throws Exception {
        transactionMethod = PayoneKlarnaCreatePaymentTransactionMethodProvider.of();
    }

    @Test
    public void whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled() {
        PaymentMethodInfo paymentInterface = PaymentMethodInfoBuilder.of()
                .paymentInterface("mockPaymentInterface")
                .method(INVOICE_KLARNA)
                .build();
        applyPaymentStubbing(paymentInterface, Money.of(666.66, "INR"));

        super.whenCreateTransactionFunctionApplied_addTransactionWithExpectedValuesIsCalled(CHARGE, Money.of(666.66, "INR"));
    }

    @Test
    public void handleSuccessfulServiceCall() throws Exception {
        String redirectUrl="www.redirectUrl.de";
        when(customFields.getFieldAsString(Mockito.eq(REDIRECT_URL))).thenReturn(redirectUrl);
        when(payment.getCustom()).thenReturn(customFields);
        PaymentTransactionCreationResult creationResult = transactionMethod.handleSuccessfulServiceCall(payment);
        assertThat(creationResult).isNotNull();
        assertThat(creationResult.getRelatedPaymentObject()).contains(payment);
        assertThat(creationResult.getException()).isNotPresent();

        assertThat(creationResult.getOperationResult()).isEqualTo(OperationResult.SUCCESS);

        assertThat(creationResult.getHandlingTask()).isNotNull();
        assertThat(creationResult.getHandlingTask().getAction()).isEqualTo(ShopAction.REDIRECT);
        assertThat(creationResult.getHandlingTask().getRedirectUrl().get()).isEqualTo(redirectUrl);

        assertThat(creationResult.getMessage()).isNotPresent();
    }
}