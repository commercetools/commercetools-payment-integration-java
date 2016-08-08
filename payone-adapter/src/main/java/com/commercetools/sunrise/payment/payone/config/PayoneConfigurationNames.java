package com.commercetools.sunrise.payment.payone.config;

/**
 * Created by mgatz on 8/8/16.
 */
public interface PayoneConfigurationNames {
    static final String HTTP_BASIC_AUTH_USER = "PAYONE_AUTH_USER";
    static final String HTTP_BASIC_AUTH_PASS = "PAYONE_AUTH_PASS";
    static final String  HANDLE_URL = "PayoneHandleURL";
    static final String REDIRECT_URL = "redirectUrl";
    static final String SUCCESS_URL = "successUrl";
    static final String CANCEL_URL = "cancelUrl";
    static final String ERROR_URL = "errorUrl";
    static final String CREDIT_CARD_FORCE_3D_SECURE = "force3DSecure";
}
