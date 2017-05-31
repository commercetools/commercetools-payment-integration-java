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

    // KLARNA specific options
    static final String GENDER = "gender"; // KlarnaConstants.Gender
    static final String IP = "ip"; // IP address, IPv4 or IPv6
    static final String BIRTHDAY = "birthday"; // Date of birth (YYYYMMDD), Mandatory for Germany, Netherlands and Austria
}
