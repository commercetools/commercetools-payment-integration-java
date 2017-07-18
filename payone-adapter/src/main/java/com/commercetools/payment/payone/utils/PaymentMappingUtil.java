package com.commercetools.payment.payone.utils;

import com.commercetools.payment.model.PaymentInteractionData;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Mapping util methods to map {@link PaymentInteractionData} values to {@link CustomFieldsDraftBuilder}.
 */
public final class PaymentMappingUtil {

    /**
     * Map values by names in the {@code properties} list from {@code supplier}'s config map to {@code acceptor}'s
     * custom fields. The properties will have have the same name in both acceptor and supplier. No any conditions
     * or values formatting are applied. Missing (<b>null</b>) values from {@code acceptor} are mapped as <b>null</b>.
     * <p>
     * For optional fields mapping see
     * {@link #mapDraftCustomFieldsIfExist(CustomFieldsDraftBuilder, PaymentInteractionData, List)}
     * </p>
     *
     * @param acceptor   {@link CustomFieldsDraftBuilder} to which map the properties
     * @param supplier   {@link PaymentInteractionData} from which to read the properties.
     * @param properties list of string properties name to be mapped from {@code supplier} config to {@code acceptor}
     *                   custom fields.
     * @return same {@code acceptor} instance.
     * @see #mapDraftCustomFieldsIfExist(CustomFieldsDraftBuilder, PaymentInteractionData, List)
     * @see #mapDraftCustomFields(CustomFieldsDraftBuilder, PaymentInteractionData, List, Function)
     */
    public static CustomFieldsDraftBuilder mapDraftCustomFields(@Nonnull CustomFieldsDraftBuilder acceptor,
                                                                @Nonnull PaymentInteractionData supplier,
                                                                @Nonnull List<String> properties) {
        return mapDraftCustomFields(acceptor, supplier, properties, t -> t);
    }

    /**
     * Map values by names in the {@code properties} list from {@code supplier}'s config map to {@code acceptor}'s
     * custom fields. The properties will have have the same name in both acceptor and supplier. No any conditions
     * are applied. Before setting value to the {@code acceptor} it will be processed (formatted)
     * by {@code valueFormatter}.
     *
     * @param acceptor       {@link CustomFieldsDraftBuilder} to which map the properties
     * @param supplier       {@link PaymentInteractionData} from which to read the properties.
     * @param properties     list of string properties name to be mapped from {@code supplier} config
     *                       to {@code acceptor} custom fields.
     * @param valueFormatter a function which pre-processes string value from the {@code supplier} to some specific
     *                       field type or format (for instance, map string to boolean, or map string date
     *                       to another date format). <b>Caveat:</b> the formatter function should expect that input
     *                       value could be <b>null</b>, thus functions reference like {@code String::toLowerCase}
     *                       might be unsafe to use, if some of the fields is missing or the value is <b>null</b>.
     *                       In such cases add additional checks, like:
     *                       <pre> mapDraftCustomFields(a, b, value -&gt; value != null ? value.toLowerCase() : null);
     *                                             </pre>
     *                       or use some null-safe methods, like {@link StringUtils#lowerCase(String)} in apache-commons
     * @return same {@code acceptor} instance.
     * @see #mapDraftCustomFields(CustomFieldsDraftBuilder, PaymentInteractionData, List)
     * @see #mapDraftCustomFieldsIfExist(CustomFieldsDraftBuilder, PaymentInteractionData, List)
     */
    public static CustomFieldsDraftBuilder mapDraftCustomFields(@Nonnull CustomFieldsDraftBuilder acceptor,
                                                                @Nonnull PaymentInteractionData supplier,
                                                                @Nonnull List<String> properties,
                                                                @Nonnull Function<String, Object> valueFormatter) {
        properties.forEach(propertyName ->
                acceptor.addObject(propertyName, valueFormatter.apply(supplier.getConfigByName(propertyName))));
        return acceptor;
    }

    /**
     * Same as {@link #mapDraftCustomFields(CustomFieldsDraftBuilder, PaymentInteractionData, List)}, but skips empty
     * String values.
     * <p>
     * For empty String verification {@link StringUtils#isNotEmpty(CharSequence)} is used, e.g. <b>null</b> and
     * <b>""</b> strings will be skipped from mapping, but blank (whitespace) strings will be mapped.
     * </p>
     *
     * @param acceptor   {@link CustomFieldsDraftBuilder} to which map the properties
     * @param supplier   {@link PaymentInteractionData} from which to read the properties.
     * @param properties list of string properties name to be mapped from {@code supplier} config to {@code acceptor}
     *                   custom fields.
     * @return same {@code acceptor} instance.
     */
    public static CustomFieldsDraftBuilder mapDraftCustomFieldsIfExist(@Nonnull CustomFieldsDraftBuilder acceptor,
                                                                       @Nonnull PaymentInteractionData supplier,
                                                                       @Nonnull List<String> properties) {
        properties.forEach(propertyName ->
                Optional.ofNullable(supplier.getConfigByName(propertyName))
                        .filter(StringUtils::isNotEmpty)
                        .ifPresent(propertyValue -> acceptor.addObject(propertyName, propertyValue)));
        return acceptor;
    }

    private PaymentMappingUtil() {
    }
}
