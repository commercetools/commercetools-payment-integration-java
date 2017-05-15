package com.commercetools.payment.payone.utils;

import com.commercetools.payment.domain.CreatePaymentDataBuilder;
import com.commercetools.payment.model.PaymentInteractionData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.types.CustomFieldsDraft;
import io.sphere.sdk.types.CustomFieldsDraftBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PaymentMappingUtilTest {

    @Mock
    private SphereClient sphereClient;

    @Mock
    private Cart cart;

    private CreatePaymentDataBuilder cpdb;
    private PaymentInteractionData pid;

    @Before
    public void setUp() throws Exception {
        cpdb = CreatePaymentDataBuilder.of(sphereClient, "testInterface", "testMethod", cart, "testReference");
        pid = cpdb.build();
    }

    @Test
    public void mapDraftCustomFields_withoutFormatter() throws Exception {
        cpdb.configValue("a", "b")
            .configValue("foo", "bar")
            .configValue("woot", null);

        CustomFieldsDraftBuilder cfdb = CustomFieldsDraftBuilder.ofTypeKey("testTypeKey");
        cfdb.addObject("itWasHere", "forever");

        CustomFieldsDraftBuilder customFieldsDraftBuilder = PaymentMappingUtil.mapDraftCustomFields(cfdb, pid,
                asList("a", "woot", "someMissingField"));

        assertThat(cfdb).isSameAs(customFieldsDraftBuilder);
        CustomFieldsDraft fieldsDraft = cfdb.build();

        assertThat(fieldsDraft.getFields()).isNotNull();
        assertThat(fieldsDraft.getFields().size()).isEqualTo(4);
        assertThat(fieldsDraft.getFields().get("a").asText()).isEqualTo("b");
        assertThat(fieldsDraft.getFields().get("itWasHere").asText()).isEqualTo("forever");
        assertThat(fieldsDraft.getFields().get("woot")).isNull();
        assertThat(fieldsDraft.getFields().get("foo")).isNull();
        assertThat(fieldsDraft.getFields().get("someMissingField")).isNull();
    }

    @Test
    public void mapDraftCustomFields_withBooleanFormatter() throws Exception {
        cpdb.configValue("a", "true")
                .configValue("notMapped", "true")
                .configValue("foo", "false")
                .configValue("bar", "some string")
                .configValue("trueUpper", "TRUE")
                .configValue("likeNull", null);

        CustomFieldsDraftBuilder cfdb = CustomFieldsDraftBuilder.ofTypeKey("testTypeKey");
        cfdb.addObject("itWasHere", "so won't be formatted");

        CustomFieldsDraftBuilder customFieldsDraftBuilder = PaymentMappingUtil.mapDraftCustomFields(cfdb, pid,
                asList("a", "foo", "bar", "trueUpper", "likeNull", "someMissingField"),
                Boolean::valueOf);

        assertThat(cfdb).isSameAs(customFieldsDraftBuilder);
        CustomFieldsDraft fieldsDraft = cfdb.build();

        assertThat(fieldsDraft.getFields()).isNotNull();
        assertThat(fieldsDraft.getFields().size()).isEqualTo(7);
        assertThat(fieldsDraft.getFields().get("a").asBoolean()).isTrue();
        assertThat(fieldsDraft.getFields().get("notMapped")).isNull();
        assertThat(fieldsDraft.getFields().get("foo").asBoolean()).isFalse();
        assertThat(fieldsDraft.getFields().get("bar").asBoolean()).isFalse();
        assertThat(fieldsDraft.getFields().get("trueUpper").asBoolean()).isTrue();
        assertThat(fieldsDraft.getFields().get("likeNull").asBoolean()).isFalse();
        assertThat(fieldsDraft.getFields().get("someMissingField").asBoolean()).isFalse();
    }

    @Test
    public void mapDraftCustomFields_withStringFormatter() throws Exception {
        cpdb.configValue("a", "b")
                .configValue("foo", "BAR")
                .configValue("woot", "haCK")
                .configValue("empty", "")
                .configValue("likeNull", null);

        CustomFieldsDraftBuilder cfdb = CustomFieldsDraftBuilder.ofTypeKey("testTypeKey");
        cfdb.addObject("itWasHere", "so won't be formatted");

        CustomFieldsDraftBuilder customFieldsDraftBuilder = PaymentMappingUtil.mapDraftCustomFields(cfdb, pid,
                asList("a", "foo", "woot", "empty", "likeNull", "someMissingFieldsomeMissingField"),
                StringUtils::upperCase);

        assertThat(cfdb).isSameAs(customFieldsDraftBuilder);
        CustomFieldsDraft fieldsDraft = cfdb.build();

        assertThat(fieldsDraft.getFields()).isNotNull();
        assertThat(fieldsDraft.getFields().size()).isEqualTo(7);
        assertThat(fieldsDraft.getFields().get("a").asText()).isEqualTo("B");
        assertThat(fieldsDraft.getFields().get("foo").asText()).isEqualTo("BAR");
        assertThat(fieldsDraft.getFields().get("woot").asText()).isEqualTo("HACK");
        assertThat(fieldsDraft.getFields().get("empty").asText()).isEqualTo("");
        assertThat(fieldsDraft.getFields().get("likeNull")).isNull();
        assertThat(fieldsDraft.getFields().get("itWasHere").asText()).isEqualTo("so won't be formatted");

    }

    @Test
    public void mapDraftCustomFieldsIfExist_test() throws Exception {
        cpdb.configValue("a", "b")
                .configValue("bar", "some string")
                .configValue("empty", "")
                .configValue("blank", " \n\t")
                .configValue("likeNull", null);

        CustomFieldsDraftBuilder cfdb = CustomFieldsDraftBuilder.ofTypeKey("testTypeKey");
        cfdb.addObject("itWasHere", "so won't be changed");

        CustomFieldsDraftBuilder customFieldsDraftBuilder = PaymentMappingUtil.mapDraftCustomFieldsIfExist(cfdb, pid,
                asList("batman", "a", "bar", "empty", "blank", "likeNull", "someMissingField"));

        assertThat(cfdb).isSameAs(customFieldsDraftBuilder);
        CustomFieldsDraft fieldsDraft = cfdb.build();

        assertThat(fieldsDraft.getFields()).isNotNull();
        assertThat(fieldsDraft.getFields().size()).isEqualTo(4);
        assertThat(fieldsDraft.getFields().get("batman")).isNull();
        assertThat(fieldsDraft.getFields().get("a").asText()).isEqualTo("b");
        assertThat(fieldsDraft.getFields().get("bar").asText()).isEqualTo("some string");
        assertThat(fieldsDraft.getFields().get("empty")).isNull();
        assertThat(fieldsDraft.getFields().get("blank").asText()).isEqualTo(" \n\t");
        assertThat(fieldsDraft.getFields().get("likeNull")).isNull();
        assertThat(fieldsDraft.getFields().get("someMissingField")).isNull();

    }
}