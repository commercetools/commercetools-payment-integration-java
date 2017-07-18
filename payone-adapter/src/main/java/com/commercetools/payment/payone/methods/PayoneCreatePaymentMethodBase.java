package com.commercetools.payment.payone.methods;

import com.commercetools.payment.actions.HandlingTask;
import com.commercetools.payment.actions.OperationResult;
import com.commercetools.payment.actions.ShopAction;
import com.commercetools.payment.domain.PaymentCreationResultBuilder;
import com.commercetools.payment.methods.CreatePaymentMethodBase;
import com.commercetools.payment.model.CreatePaymentData;
import com.commercetools.payment.model.PaymentCreationResult;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static com.commercetools.payment.payone.config.PayoneConfigurationNames.LANGUAGE_CODE;
import static com.commercetools.payment.payone.config.PayoneConfigurationNames.REFERENCE;
import static java.lang.String.format;

/**
 * Base class for <b>Payone</b> related payment methods.
 */
public abstract class PayoneCreatePaymentMethodBase extends CreatePaymentMethodBase {

    /**
     * @return String Payone payment method name.
     * @see PayonePaymentMethodType
     */
    abstract String getMethodType();

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
     *     <li><i>else</i> try {@link #getLanguageFromCart(Cart)} with {@code cpd.getCart()}</li>
     *     <li><i>else</i> fallback to "en" locale</li>
     * </ul>
     * @param cpd payment-cart data from which define the locale
     * @return non-null 2 characters string language name according to ISO 639 format
     * @see #getLanguageFromCart(Cart)
     */
    protected static String getLanguageFromPaymentDataOrFallback(CreatePaymentData cpd) {
        Optional<CreatePaymentData> optionalCpd = Optional.ofNullable(cpd);

        return optionalCpd
                .map(paymentData -> paymentData.getConfigByName(LANGUAGE_CODE)) // if LANGUAGE_CODE is set in config
                .orElseGet(() -> optionalCpd
                        .map(CreatePaymentData::getCart)
                        .map(CreatePaymentMethodBase::getLanguageFromCart)
                        .orElseGet(Locale.ENGLISH::getLanguage));
    }

    @Override
    public Function<CreatePaymentData, CompletionStage<PaymentCreationResult>> create() {
        return cpd ->
                addNewPayment(cpd)
                        .thenApply(payment -> PaymentCreationResultBuilder
                                .of(OperationResult.SUCCESS)
                                .payment(payment)
                                // so far for the payments we use only CONTINUE handling task action,
                                // aka "proceed to checkout page",
                                // but later a transactions creation results could be REDIRECT (like for paypal)
                                .handlingTask(HandlingTask.of(ShopAction.CONTINUE))
                                .build())
                        .exceptionally(ex -> PaymentCreationResultBuilder
                                .ofError(format("An error occurred during creation of [%s] payment object",
                                        getMethodType()), ex));
    }
}
