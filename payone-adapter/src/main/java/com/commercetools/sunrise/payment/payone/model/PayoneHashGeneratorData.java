package com.commercetools.sunrise.payment.payone.model;

import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

/**
 * Created by mgatz on 8/1/16.
 */
public class PayoneHashGeneratorData {

    public static final String KEY_AID = "aid";
    public static final String KEY_MID = "mid";
    public static final String KEY_PORTALID = "portalid";
    public static final String KEY_PMI_PORTAL_KEY = "PMI Portal Key";
    public static final String KEY_STORECARDDATA = "storecarddata";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_MODE = "mode";
    public static final String KEY_ENCODING = "encoding";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_MODE = "test";
    public static final String DEFAULT_REQUEST = "creditcardcheck";
    public static final String DEFAULT_STORECARDDATA = "yes";
    public static final String DEFAULT_PMI_PORTAL_KEY = "123";

    private final Map<String, String> sortedAttributes = new TreeMap<>();

    public static PayoneHashGeneratorData of(String aid, String mid, String portalid) {
        return new PayoneHashGeneratorData(aid, mid, portalid);
    }

    private PayoneHashGeneratorData(final String aid, final String mid, final String portalid) {
        sortedAttributes.put(KEY_AID, aid);
        sortedAttributes.put(KEY_MID, mid);
        sortedAttributes.put(KEY_PORTALID, portalid);

        // defaults
        sortedAttributes.put(KEY_ENCODING, DEFAULT_ENCODING);
        sortedAttributes.put(KEY_MODE, DEFAULT_MODE);
        sortedAttributes.put(KEY_REQUEST, DEFAULT_REQUEST);
        sortedAttributes.put(KEY_STORECARDDATA, DEFAULT_STORECARDDATA);
        sortedAttributes.put(KEY_PMI_PORTAL_KEY, DEFAULT_PMI_PORTAL_KEY);
    }

    public PayoneHashGeneratorData overrideDefaultEncoding(String encoding) {
        sortedAttributes.put(KEY_ENCODING, encoding);
        return this;
    }

    public PayoneHashGeneratorData overrideDefaultMode(String mode) {
        sortedAttributes.put(KEY_MODE, mode);
        return this;
    }

    public PayoneHashGeneratorData overrideDefaultRequest(String request) {
        sortedAttributes.put(KEY_REQUEST, request);
        return this;
    }

    public PayoneHashGeneratorData overrideDefaultStoreCardData(String storeCardData) {
        sortedAttributes.put(KEY_STORECARDDATA, storeCardData);
        return this;
    }

    public PayoneHashGeneratorData overrideDefaultPmiPortalKey(String portalKey) {
        sortedAttributes.put(KEY_PMI_PORTAL_KEY, portalKey);
        return this;
    }

    public PayoneHashGeneratorData setCustomValue(String key, String value) {
        sortedAttributes.put(key, value);
        return this;
    }

    public String build() {
        StringJoiner sj = new StringJoiner("");
        sortedAttributes.entrySet().stream().forEachOrdered(entry -> sj.add(entry.getValue()));
        return sj.toString();
    }
}
