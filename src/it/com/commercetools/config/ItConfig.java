package com.commercetools.config;

import io.sphere.sdk.client.SphereClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.Properties;

/**
 * Integration test wide configuration constants.
 *
 * <p>The class is used to access commercetools sphere client configuration values (project key, id and so on),
 * as same as other test configuration values (like {@link #getPayoneIntegrationUrl()}.
 * The values may be set in two ways:<ul>
 *     <li><i><b>it.properties</b></i> file in the <i>resources/</i> directory (excluded from version control)</li>
 *     <li>via OS environment variables</li>
 * </ul>
 *
 * <b>The properties file values have precedence</b> over environment variables.</p>
 *
 * <p>The value keys should use prefixes: <b>{@code ct.}</b> and <b>{@code CT_}</b> respectively
 * for properties and environment variables. For instance:<br/>
 * in properties file:
 * <pre>
 *  ct.projectKey=project-payment-test
 *  ct.payoneIntegrationUrl=http://localhost:8080/commercetools/handle/payments/
 * </pre>
 * in environment variables:
 * <pre>
 *  export CT_PROJECT_KEY="project-payment-test"
 *  export CT_PAYONE_INTEGRATION_URL="http://localhost:8080/commercetools/handle/payments/"
 * </pre>
 * </p>
 *
 * @see SphereClientConfig SphereClientConfig for more details about config values and prefixes.
 *
 */
public class ItConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItConfig.class);

    private static final String IT_PROPERTIES_FILE = "it.properties";

    /**
     * These properties inherit behavior of {@link SphereClientConfig}, but for environment prefix we use 2 constants,
     * because sphere concats underscore in the environment preffix, but but doesn't concat period in the properties
     * prefix.
     */
    private static final String PROPERTIES_PREFIX = "ct.";
    private static final String SPHERE_ENVIRONMENT_PREFIX = "CT"; // SphereClientConfig concatenates underscore itself
    private static final String ENVIRONMENT_PREFIX = SPHERE_ENVIRONMENT_PREFIX + "_";

    /**
     * Try to get {@link SphereClientConfig} in the next order:<ol>
     *     <li>read from {@link #IT_PROPERTIES} if it has values ({@code IT_PROPERTIES.size() &gt; 0}</li>
     *     <li>read from environment variables</li>
     * </ol>
     *
     * @return created {@link SphereClientConfig}
     */
    public static SphereClientConfig getClientConfig() {
        return IT_PROPERTIES.size() > 0
                ? SphereClientConfig.ofProperties(IT_PROPERTIES, PROPERTIES_PREFIX)
                : SphereClientConfig.ofEnvironmentVariables(SPHERE_ENVIRONMENT_PREFIX);
    }

    /**
     * Full URL to payone integration service with port and <b>trailing slash</b>.
     * Example:
     * <pre>http://localhost:8080/commercetools/handle/payments/</pre>
     * (if local service is running)
     * <p>The key name is:<ul>
     *     <li><b>{@code ct.payoneIntegrationUrl}</b> in the properties file</li>
     *     <li><b>{@code CT_PAYONE_INTEGRATION_URL}</b> in the environment variables </li>
     * </ul></p>
     */
    public static String getPayoneIntegrationUrl() {
        return getValueByName("payoneIntegrationUrl", "PAYONE_INTEGRATION_URL");
    }

    /**
     * Properties set read from {@link #IT_PROPERTIES_FILE}. If file not found or empty - has zero size.
     */
    private static final Properties IT_PROPERTIES = new Properties();

    static {
        // try to find and read the properties file.
        // Since some build tools used to execute the tests in parallel - we use static  blocking initialization

        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(IT_PROPERTIES_FILE)) {
            if (in != null) {
                LOGGER.info("Read client config from the properties file \"{}\"", IT_PROPERTIES_FILE);
                IT_PROPERTIES.load(in);
            } else {
                LOGGER.info("Read client config from the environment variables");
                IT_PROPERTIES.clear(); // clean IT_PROPERTIES mean use environment variables.
            }
        } catch (Exception e) {
            LOGGER.warn("IT Tests properties file \"{}\" reading error: {} "
                    + "Fallback to environment variables", IT_PROPERTIES_FILE, e.getMessage());
        }
    }

    /**
     * Try to read value in the declared order:<br/>
     *   1. Try to get from properties file, if exists.
     *   2. Try to get from environment variables.
     *
     * @param propName name of property (without prefix)
     * @param envName name of environment value (without prefix)
     * @return value of the looking property from properties file (if the file and the property both exist)
     * or from environment variable.
     */
    @Nullable
    private static String getValueByName(@Nonnull String propName, @Nonnull String envName) {
        String propKey = PROPERTIES_PREFIX + propName;
        return IT_PROPERTIES.containsKey(propKey)
                ? IT_PROPERTIES.getProperty(propKey)
                : System.getenv(ENVIRONMENT_PREFIX + envName);
    }
}
