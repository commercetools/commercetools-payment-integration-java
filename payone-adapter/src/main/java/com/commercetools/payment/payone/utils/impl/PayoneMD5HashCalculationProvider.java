package com.commercetools.payment.payone.utils.impl;

import com.commercetools.payment.payone.utils.PayoneHashCalculationProvider;

public class PayoneMD5HashCalculationProvider implements PayoneHashCalculationProvider {

    @Override
    public String getAlgorithm() {
        return "MD5";
    }
}
