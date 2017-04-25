package com.commercetools.payment.payone.utils.impl;

import com.commercetools.payment.payone.utils.PayoneHashCalculationProvider;

public class PayoneSHA384HashCalculationProvider implements PayoneHashCalculationProvider {

    @Override
    public String getAlgorithm() {
        return "SHA-384";
    }
}
