package com.commercetools.payment.payone.config;

/**
 * Created by mgatz on 8/8/16.
 */
public interface PayoneConfigurationNames {
    static final String LANGUAGE_CODE = "languageCode";
    static final String REFERENCE = "reference";
    static final String HANDLE_URL = "PayoneHandleURL";
    static final String REDIRECT_URL = "redirectUrl";
    static final String SUCCESS_URL = "successUrl";
    static final String CANCEL_URL = "cancelUrl";
    static final String ERROR_URL = "errorUrl";
    static final String CREDIT_CARD_FORCE_3D_SECURE = "force3DSecure";
    static final String CREDIT_CARD_CARD_DATA_PLACEHOLDER = "cardDataPlaceholder";
    static final String CREDIT_CARD_TRUNCATED_CARD_NUMBER = "truncatedCardNumber";
    static final String CREDIT_CARD_CARD_HOLDER_NAME = "cardHolderName";
    static final String CREDIT_CARD_EXPIRY_DATE = "cardExpiryDate";
    static final String CREDIT_CARD_CARD_NETWORK = "cardNetwork";
    static final String PAID_TO_IBAN = "paidToIBAN";
    static final String PAID_TO_BIC = "paidToBIC";
    static final String PAID_TO_NAME = "paidToAccountHolderName";
    static final String BANK_GROUP_TYPE ="bankGroupType";

    /**
     * Customer's gender. Single character {@code m} or {@code f}. Mandatory for <i>Klarna</i>.
     */
    static final String GENDER = "gender"; // "m" or "f"

    /**
     * Customer's IPv4/IPv6 address. Mandatory for Klarna.
     */
    static final String IP = "ip";

    /**
     * Customer's date of birth. Mandatory for <i>Klarna</i> in Germany, Netherlands and Austria.
     * <p>
     * In CTP project the value is type of Date and must be in {@link java.time.LocalDate#parse(CharSequence)}
     * parsable format, like {@code YYYY-MM-DD}.
     * <p>
     * <b>Note:</b> Payone API requires birthday as a string in {@code YYYYMMDD} format.
     */
    static final String BIRTHDAY = "birthday";

    /**
     * Customer's telephone number. Mandatory for <i>Klarna</i>.
     */
    static final String TELEPHONENUMBER = "telephonenumber";
}
