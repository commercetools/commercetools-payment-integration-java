package com.commercetools.payment.service;

import com.commercetools.payment.domain.PaymentServiceProvider;
import io.sphere.sdk.payments.PaymentMethodInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

import static com.commercetools.payment.payone.config.PayoneConstants.PAYONE_INTERFACE_ID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test when both INTERNAL:FREE and PAYONE service providers are available in the classpath.
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class PaymentAdapterServiceImplTest {

    private static PaymentAdapterServiceImpl pas;

    /**
     * The service is stateless thus we could use single static instance.
     */
    @BeforeClass
    public static void setUpClass() {
        pas = new PaymentAdapterServiceImpl();
    }

    @Test
    public void findAllPaymentServiceProviders() throws Exception {
        List<PaymentServiceProvider> allPaymentServiceProviders = pas.findAllPaymentServiceProviders();

        assertThat(allPaymentServiceProviders).isNotNull();
        assertThat(allPaymentServiceProviders.size()).isEqualTo(2); // shell be changed if new PSP providers are added

        assertThat(allPaymentServiceProviders.stream()
                .filter(psp -> "PAYONE".equals(psp.getId()))
                .findFirst()
                .orElse(null)).isNotNull();

        assertThat(allPaymentServiceProviders.stream()
                .filter(psp -> "INTERNAL".equals(psp.getId()))
                .findFirst()
                .orElse(null)).isNotNull();
    }

    @Test
    public void findAvailablePaymentMethods_all() throws Exception {
        List<PaymentMethodInfo> availablePaymentMethods = pas.findAvailablePaymentMethods();
        assertThat(availablePaymentMethods).isNotNull();
        assertThat(availablePaymentMethods.size()).isEqualTo(7 + 1); // 7 PAYONE methods + 1 INTERNAL:FREE

        PaymentMethodInfo payoneCc = getByInterfaceAndMethod(availablePaymentMethods, PAYONE_INTERFACE_ID, "CREDIT_CARD");
        assertThat(payoneCc).isNotNull();

        PaymentMethodInfo payoneBta = getByInterfaceAndMethod(availablePaymentMethods, PAYONE_INTERFACE_ID, "BANK_TRANSFER-ADVANCE");
        assertThat(payoneBta).isNotNull();

        PaymentMethodInfo payoneKlv = getByInterfaceAndMethod(availablePaymentMethods, PAYONE_INTERFACE_ID, "INVOICE-KLARNA");
        assertThat(payoneKlv).isNotNull();

        PaymentMethodInfo internal = getByInterfaceAndMethod(availablePaymentMethods, "INTERNAL", "FREE");
        assertThat(internal).isNotNull();
    }

    @Test
    public void findAvailablePaymentMethods_filtered() throws Exception {
        List<PaymentMethodInfo> onlyBankPayments = pas.findAvailablePaymentMethods(
                allMethods -> allMethods.stream()
                        .filter(pmi -> StringUtils.containsIgnoreCase(pmi.getMethod(), "bank"))
                        .collect(Collectors.toList()));

        assertThat(onlyBankPayments).isNotNull();
        assertThat(onlyBankPayments.size()).isEqualTo(4); // 4 payone bank transfer methods

        List<PaymentMethodInfo> onlyFree = pas.findAvailablePaymentMethods(
                allMethods -> allMethods.stream()
                        .filter(pmi -> StringUtils.containsIgnoreCase(pmi.getMethod(), "free"))
                        .collect(Collectors.toList()));

        assertThat(onlyFree).isNotNull();
        assertThat(onlyFree.size()).isEqualTo(1);
    }

    @Test
    public void getPaymentMethodInfo() throws Exception {
        PaymentMethodInfo paypal = pas.getPaymentMethodInfo(PAYONE_INTERFACE_ID, "WALLET-PAYPAL")
                                                            .orElse(null);
        assertThat(paypal).isNotNull();
        assertThat(paypal.getMethod()).isEqualTo("WALLET-PAYPAL");
        assertThat(paypal.getName()).isNotNull();
        assertThat(paypal.getName().get("de")).isEqualTo("Paypal");
        assertThat(paypal.getName().get("en")).isEqualTo("Paypal");

        PaymentMethodInfo free = pas.getPaymentMethodInfo("INTERNAL", "FREE")
                                                            .orElse(null);
        assertThat(free).isNotNull();
        assertThat(free.getMethod()).isEqualTo("FREE");
        assertThat(free.getName()).isNotNull();
        assertThat(free.getName().get("de")).isEqualTo("kostenlos");
        assertThat(free.getName().get("en")).isEqualTo("free");
    }



    private PaymentMethodInfo getByInterfaceAndMethod(List<PaymentMethodInfo> list, String interfaceId, String method) {
        return list.stream()
                .filter(pmi -> interfaceId.equals(pmi.getPaymentInterface()))
                .filter(pmi -> method.equals(pmi.getMethod()))
                .findFirst()
                .orElse(null);
    }
}
