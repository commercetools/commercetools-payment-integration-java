package com.commercetools.payment;

import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.domain.CreatePaymentTransactionDataBuilder;
import com.commercetools.payment.payone.config.PayonePaymentMethodKeys;
import com.commercetools.payment.model.PaymentCreationResult;
import com.commercetools.payment.model.PaymentTransactionCreationResult;
import com.commercetools.payment.service.PaymentAdapterService;
import io.sphere.sdk.types.CustomFields;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

import static com.commercetools.config.ItConfig.getPayoneIntegrationUrl;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.*;
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
        PaymentTransactionCreationResult paymentTransactionCreationResult = PaymentAdapterService.of()
                .createPaymentTransaction(
                        CreatePaymentTransactionDataBuilder
                                .of(client, paymentCreationResult.getRelatedPaymentObject().get().getId())
                                .setConfigValue(HANDLE_URL, getPayoneIntegrationUrl())
                                .build())
                .toCompletableFuture().get();

        assertPaymentTransactionObjectCreation(paymentTransactionCreationResult);
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
