package com.commercetools.sunrise.payment.actions;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by mgatz on 7/20/16.
 */
public class HandlingTaskTest {
    @Test
    public void createNew() {
        assertThat(HandlingTask.of(ShopAction.CONTINUE), instanceOf(HandlingTask.class));
        assertThat(HandlingTask.of(ShopAction.REDIRECT).getAction(), is(ShopAction.REDIRECT));
    }

    @Test
    public void addRedirectURL() throws MalformedURLException {
        URL toTest = new URL("https://dev.commercetools.com");

        // optional test without a url given
        HandlingTask t1 = HandlingTask.of(ShopAction.CONTINUE);
        assertThat(t1.getRedirectUrl().isPresent(), is(false));

        HandlingTask t2 = HandlingTask.of(ShopAction.CONTINUE).redirectUrl(toTest);
        assertThat(t2.getRedirectUrl().get(), is(toTest));
    }

    @Test
    public void addAdditionalData() {
        String key = "foo";
        Object value = "bar";

        HandlingTask t = HandlingTask.of(ShopAction.CONTINUE).addData(key, value);
        assertThat(t.getAdditionalData().size(), is(1));
        assertThat(t.getAdditionalData().get(key), is(value));
    }
}