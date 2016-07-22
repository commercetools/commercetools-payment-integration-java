package com.commercetools.sunrise.payment.payone;

import com.commercetools.sunrise.payment.utils.PaymentPropertiesLoadingHelper;

/**
 * Created by mgatz on 7/22/16.
 */
public class PayoneHelper {
    /**
     * Create a new {@link PaymentPropertiesLoadingHelper} instance for the payone configuration file.
     * @return a payment properties loading helper for the payone configuration
     */
    public static PaymentPropertiesLoadingHelper getPayonePropertiesLoadingHelper() {
        // TODO: in future have this cached and beeing reusable in any way
        return PaymentPropertiesLoadingHelper.createFromResource("methods/payone.properties");
    }

    /**
     * Get the Payone Service Id to be used all over the plattform.
     * @return the Payone service Id
     */
    public static String getPaymentServiceProviderId() {
        return getPayonePropertiesLoadingHelper().getPaymentServiceId();
    }
}
