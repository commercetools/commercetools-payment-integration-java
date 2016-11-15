package com.commercetools.config;

/**
 * Integration test wide configuration constants. Some of them used to be read form OS environment variables.
 */
public class ItConfig {

    /**
     * Sphere project access values. Find them in <a href="https://admin.sphere.io/">https://admin.sphere.io/</a>
     */
    public static final String CT_PROJECT_KEY = System.getenv("CT_PROJECT_KEY");
    public static final String CT_CLIENT_ID = System.getenv("CT_CLIENT_ID");
    public static final String CT_CLIENT_SECRET = System.getenv("CT_CLIENT_SECRET");

    /**
     * Full URL to payone integration service with port and trailing slash.
     * Example: <i>http://localhost:8080/commercetools/handle/payments/</i> (if local service is run)
     */
    public static final String CT_PAYONE_INTEGRATION_URL = System.getenv("CT_PAYONE_INTEGRATION_URL");
}
