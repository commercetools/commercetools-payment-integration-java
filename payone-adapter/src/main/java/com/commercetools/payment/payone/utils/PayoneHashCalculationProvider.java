package com.commercetools.payment.payone.utils;

import com.commercetools.payment.payone.utils.impl.PayoneMD5HashCalculationProvider;
import com.commercetools.payment.payone.utils.impl.PayoneSHA384HashCalculationProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface PayoneHashCalculationProvider {
    public static PayoneHashCalculationProvider of(String hashAlgo) {
        if("MD5".equals(hashAlgo)) {
            return new PayoneMD5HashCalculationProvider();
        }

        return new PayoneSHA384HashCalculationProvider();
    }

    String getAlgorithm();

    default String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(getAlgorithm());
            byte[] hashBytes = md.digest(data.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte hashByte : hashBytes) {
                int b = hashByte & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return data;
        }
    }


}
