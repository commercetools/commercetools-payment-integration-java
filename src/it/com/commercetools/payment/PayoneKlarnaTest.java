package com.commercetools.payment;

import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.payment.payone.config.PayonePaymentMethodKeys;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.Transaction;
import io.sphere.sdk.types.CustomFields;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.commercetools.config.ItConfig.getPayoneIntegrationUrl;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
import static io.sphere.sdk.payments.TransactionState.FAILURE;
import static io.sphere.sdk.payments.TransactionState.INITIAL;
import static io.sphere.sdk.payments.TransactionType.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

public class PayoneKlarnaTest extends BasePayoneTest {

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
        String reference = generateTestPayoneReference("klv");
        PaymentCreationResult paymentCreationResult = PaymentAdapterService.of()
                .createPayment(
                        CreatePaymentDataBuilder.of(
                                client,
                                "PAYONE",
                                PayonePaymentMethodKeys.INVOICE_KLARNA,
                                cart,
                                reference)
                                .configValue(GENDER, "female")
                                .configValue(IP, "1.7.4.6")
                                .configValue(BIRTHDAY, "1956-08-31")
                                .configValue(TELEPHONENUMBER, "472634876234")
                                .build())
                .toCompletableFuture().get();

        assertKlarnaPaymentCreation(paymentCreationResult, reference);

        // user clicked "buy now" -> create transaction, trigger handle payment, return updated payment object
        PaymentTransactionCreationResult ptcr = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl())
                                .build())
                .toCompletableFuture().get();

        // with random data Klarna payment is expected to failure

        assertThat(ptcr).isNotNull();

        assertThat(ptcr.getOperationResult())
                .withFailMessage("Redirect URL provided from Klarna is not available.Error code: ERROR 1350, Error message: Parameter {email} faulty or missing]]");

        Payment payment = ptcr.getRelatedPaymentObject().orElse(null);
        assertThat(payment).isNotNull();
        List<Transaction> transactions = payment.getTransactions();
        final int size = transactions.size();
        assertThat(size).isGreaterThan(0); // at least one has to be there
        Transaction transaction = transactions.get(size - 1);
        assertThat(transaction.getType()).isEqualTo(AUTHORIZATION);

        // PaymentAdapterService.createPaymentTransaction() creates transaction and immediately handles the payment,
        // thus after execution the status switches from INITIAL to some other
        assertThat(transaction.getState()).isNotEqualByComparingTo(INITIAL);
        assertThat(transaction.getState()).isEqualTo(FAILURE);
    }

    private void assertKlarnaPaymentCreation(PaymentCreationResult pcr, String reference) {
        assertPaymentObjectCreation(pcr, reference);

        CustomFields customFields = getCustomFields(pcr);

        assertThat(customFields.getFieldAsString(GENDER)).isEqualTo("female");
        assertThat(customFields.getFieldAsString(IP)).isEqualTo("1.7.4.6");
        assertThat(customFields.getFieldAsDate(BIRTHDAY)).isEqualTo(LocalDate.of(1956, 8, 31));
        assertThat(customFields.getFieldAsString(TELEPHONENUMBER)).isEqualTo("472634876234");
    }

}
