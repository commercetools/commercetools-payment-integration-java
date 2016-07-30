package com.commercetools.sunrise.payment.actions;

import org.junit.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mgatz on 7/20/16.
 */
public class HandlingTaskTest {
    @Test
    public void createNew() {
        assertThat(HandlingTask.of(ShopAction.CONTINUE)).isInstanceOf(HandlingTask.class);
        assertThat(HandlingTask.of(ShopAction.REDIRECT_BEFORE_CHECKOUT).getAction()).isEqualTo(ShopAction.REDIRECT_BEFORE_CHECKOUT);
    }

    @Test
    public void addRedirectURL() throws MalformedURLException {
        String toTest = "https://dev.commercetools.com";

        // optional test without a url given
        HandlingTask t1 = HandlingTask.of(ShopAction.CONTINUE);
        assertThat(t1.getRedirectUrl().isPresent()).isFalse();

        HandlingTask t2 = HandlingTask.of(ShopAction.CONTINUE).redirectUrl(toTest);
        assertThat(t2.getRedirectUrl().get()).isEqualTo(toTest);
    }

    @Test
    public void addAdditionalData() {
        String key = "foo";
        Object value = "bar";

        HandlingTask t = HandlingTask.of(ShopAction.CONTINUE).addData(key, value);
        assertThat(t.getAdditionalData().size()).isEqualTo(1);
        assertThat(t.getAdditionalData().get(key)).isEqualTo(value);
    }
}