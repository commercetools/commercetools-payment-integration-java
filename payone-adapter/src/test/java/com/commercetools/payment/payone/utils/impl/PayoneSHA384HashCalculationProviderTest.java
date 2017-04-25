package com.commercetools.payment.payone.utils.impl;

import com.commercetools.payment.payone.utils.PayoneHashCalculationProvider;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by mgatz on 8/1/16.
 */
public class PayoneSHA384HashCalculationProviderTest {
    private final String input = "This1ValueHasToBeHashed";
    private final String expected = "7c719c1c147a6bde6acadd76bbfde3aba6b2eca8158fe3d325e928b4a2b15b632828163fa4cb7dc7ccff0d93ed3a5587";

    @Test
    public void generateHash() {
        PayoneHashCalculationProvider provider = PayoneHashCalculationProvider.of("SHA-384");
        assertThat(provider.generateHash(input)).isEqualTo(expected);
    }
}