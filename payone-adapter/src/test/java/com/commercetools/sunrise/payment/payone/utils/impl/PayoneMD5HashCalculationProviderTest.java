package com.commercetools.sunrise.payment.payone.utils.impl;

import com.commercetools.sunrise.payment.payone.utils.PayoneHashCalculationProvider;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by mgatz on 8/1/16.
 */
public class PayoneMD5HashCalculationProviderTest {
    private final String input = "This1ValueHasToBeHashed";
    private final String expected = "3c9f34de9283475a3e2741f0cc11bb32";

    @Test
    public void generateHash() {
        PayoneHashCalculationProvider provider = PayoneHashCalculationProvider.of("MD5");
        assertThat(provider.generateHash(input)).isEqualTo(expected);
    }
}