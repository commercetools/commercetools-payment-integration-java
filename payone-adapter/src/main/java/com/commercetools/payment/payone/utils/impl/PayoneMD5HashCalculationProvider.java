package com.commercetools.payment.payone.utils.impl;

import com.commercetools.payment.payone.utils.PayoneHashCalculationProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mgatz on 8/1/16.
 */
public class PayoneMD5HashCalculationProvider implements PayoneHashCalculationProvider {
    @Override
    public String generateHash(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(data.getBytes());
            String hex = "";
            for(int i = 0; i < hashBytes.length ; i++) {
                int b = hashBytes[i] & 0xff;
                if (Integer.toHexString(b).length() == 1) {
                    hex = hex + "0";
                }
                hex = hex + Integer.toHexString(b);
            }
            return hex;
        } catch (NoSuchAlgorithmException e) {
            return data;
        }
    }
}
