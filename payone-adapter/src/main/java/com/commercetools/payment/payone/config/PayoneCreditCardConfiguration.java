package com.commercetools.payment.payone.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mgatz on 8/10/16.
 */
public class PayoneCreditCardConfiguration {
    private String javascriptInclude;

    @JsonCreator
    private PayoneCreditCardConfiguration(
            @JsonProperty("javascriptInclude") final String javascriptInclude) {
        this.javascriptInclude = javascriptInclude;
    }

    public String getJavascriptInclude() {
        return javascriptInclude;
    }
}
