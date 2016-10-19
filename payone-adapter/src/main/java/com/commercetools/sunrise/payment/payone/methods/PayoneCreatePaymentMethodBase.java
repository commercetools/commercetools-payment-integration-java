package com.commercetools.sunrise.payment.payone.methods;

import com.commercetools.sunrise.payment.methods.CreatePaymentMethodBase;
import com.commercetools.sunrise.payment.model.CreatePaymentData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static com.commercetools.sunrise.payment.payone.config.PayoneConfigurationNames.REFERENCE;

/**
 * Base class for <b>Payone</b> related payment methods.
 */
public abstract class PayoneCreatePaymentMethodBase extends CreatePaymentMethodBase {

    /**
     * @return String Payone payment method name.
     * @see PayonePaymentMethodType
     */
    abstract protected String getMethodType();

    /**
     * @return new instance of {@link CustomFieldsDraftBuilder} with implementation specific type key
     * (see {@link #getMethodType()})
     */
    protected CustomFieldsDraftBuilder createCustomFieldsBuilder() {
        return CustomFieldsDraftBuilder.ofTypeKey(getMethodType());
    }

    /**
     * By default all Payone custom fields have "languageCode" and "reference" values from payment data.
     * @param cpd payment data from which fetch languageCode and reference
     * @return CustomFieldsDraftBuilder of related type key (see {@link #getMethodType()})
     * with pre-filled default values from {@code cpd}
     * @see #createCustomFieldsBuilder()
     */
    protected CustomFieldsDraftBuilder createCustomFieldsBuilder(CreatePaymentData cpd) {
        return createCustomFieldsBuilder()
                .addObject(REFERENCE, cpd.getReference())
                .addObject(LANGUAGE_CODE, getLanguageFromPaymentDataOrFallback(cpd));
    }

    /**
     * Try to define payment locale (two characters ISO 639) in the next order:<ul>
     *     <li>from custom payment data config, e.g. {@code cpd.getConfigByName(LANGUAGE_CODE)}
     *         (if not <b>null</b>)</li>
     *     <li><i>else</i> try {@link #getLanguageFromCartOrFallback(Cart)} with {@code cpd.getCart()}</li>
     *     <li><i>else</i> fallback to "en" locale</li>
     * </ul>
     * @param cpd payment-cart data from which define the locale
     * @return non-null 2 characters string language name according to ISO 639 format
     * @see #getLanguageFromCartOrFallback(Cart)
     */
    protected static String getLanguageFromPaymentDataOrFallback(CreatePaymentData cpd) {
        Optional<CreatePaymentData> optionalCpd = Optional.ofNullable(cpd);

        return optionalCpd
                .map(paymentData -> paymentData.getConfigByName(LANGUAGE_CODE)) // if LANGUAGE_CODE is set in config
                .orElseGet(() -> optionalCpd
                        .map(CreatePaymentData::getCart)
                        .map(CreatePaymentMethodBase::getLanguageFromCartOrFallback)
                        .orElseGet(Locale.ENGLISH::getLanguage)); // fallback to en
    }
}
