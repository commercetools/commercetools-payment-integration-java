package com.commercetools.payment.payone.methods;

/**
 * Payone available payment method types.
 */
public enum PayonePaymentMethodType {
    PAYMENT_CREDIT_CARD("payment-CREDIT_CARD"),
    PAYMENT_WALLET("payment-WALLET"),
    PAYMENT_CASH_ADVANCE("payment-CASH-ADVANCE"),

    /**
     * It's a generic method for all <em>payment-BANK_TRANSFER</em> types that just uses redirect
     */
    PAYMENT_BANK_TRANSFER("payment-BANK_TRANSFER"),

    PAYMENT_INVOICE_KLARNA("payment-INVOICE-KLARNA");

    private String value;

    PayonePaymentMethodType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
