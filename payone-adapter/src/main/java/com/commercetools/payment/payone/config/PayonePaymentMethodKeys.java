package com.commercetools.payment.payone.config;

/**
 * Method names/keys supported by the library.
 * Note: this names set must respect names in
 * {@code com.commercetools.pspadapter.payone.domain.ctp.paymentmethods.MethodKeys}
 * in <i>commercetools-payone-integration</i> service.
 */
public class PayonePaymentMethodKeys {

    /**
     * Credit Card
     * Generic Key for any type of Credit Card (recommended, implementation will autodetect specific variant)
     */
    public static final String CREDIT_CARD = "CREDIT_CARD";

    /**
     * PayPal
     * The PayPal Wallet
     */
    public static final String WALLET_PAYPAL = "WALLET-PAYPAL";

    /**
     * Money received by merchant before delivery via Bank Transfer
     */
    public static final String BANK_TRANSFER_ADVANCE = "BANK_TRANSFER-ADVANCE";

    /**
     * pay via you online banking system
     */
    public static final String BANK_TRANSFER_SOFORTUEBERWEISUNG = "BANK_TRANSFER-SOFORTUEBERWEISUNG";

    /**
     * Switzerland specific
     */
    public static final String BANK_TRANSFER_POSTFINANCE_EFINANCE = "BANK_TRANSFER-POSTFINANCE_EFINANCE";

    /**
     * Switzerland specific
     */
    public static final String BANK_TRANSFER_POSTFINANCE_CARD = "BANK_TRANSFER-POSTFINANCE_CARD";

    public static final String INVOICE_KLARNA = "INVOICE-KLARNA";

}
