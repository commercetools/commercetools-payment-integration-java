package com.commercetools.sunrise.payment.payone.utils;

import com.commercetools.sunrise.payment.payone.utils.impl.PayoneMD5HashCalculationProvider;
import com.commercetools.sunrise.payment.payone.utils.impl.PayoneSHA384HashCalculationProvider;

/**
 * Created by mgatz on 8/1/16.
 */
public interface PayoneHashCalculationProvider {
    public static PayoneHashCalculationProvider of(String hashAlgo) {
        if("MD5".equals(hashAlgo)) {
            return new PayoneMD5HashCalculationProvider();
        }

        return new PayoneSHA384HashCalculationProvider();
    }

    public String generateHash(String data);
}
