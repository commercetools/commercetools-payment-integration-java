package com.commercetools.util;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.AddressBuilder;
import io.sphere.sdk.models.DefaultCurrencyUnits;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.commercetools.config.ItConfig.getClientConfig;

/**
 * Created by mgatz on 7/10/16.
 */
public class IntegrationTestUtils {
    /**
     * @return a newly created test sphere client
     */
    public static BlockingSphereClient createClient() {
        return BlockingSphereClient.of(
                SphereClientFactory.of().createClient(getClientConfig()),
                10, TimeUnit.SECONDS);
    }

    /**
     * Returns the first product found inside the client connected project.
     * Executes blocking.
     *
     * @param client the client handling the connection
     * @return the product projection
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ProductProjection getProduct(BlockingSphereClient client) throws ExecutionException, InterruptedException {
        return getProduct(client, 0l);
    }

    /**
     * Returns the product found inside the client connected project with the passed offset.
     * Executes blocking.
     *
     * @param client the client handling the connection
     * @param offset the offset used to get another product
     * @return the product projection
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ProductProjection getProduct(BlockingSphereClient client, Long offset) throws ExecutionException, InterruptedException {
        ProductProjectionQuery query = ProductProjectionQuery.ofCurrent()
                .withSort(s -> s.createdAt().sort().desc()).withOffset(offset).withLimit(1);
        return client.executeBlocking(query).head().orElseThrow(() -> new AssertionError("ProductProjection not found"));
    }

    /**
     * Creates a new cart and adds the passed product as a new line item using a random quantiy between 1 and 10.
     *
     * @param client the client handling the connection
     * @return the created cart
     */
    public static Cart createTestCartFromProduct(CountryCode countryCode, BlockingSphereClient client,
                                                 Integer productNumber) throws ExecutionException, InterruptedException {
        Address address = AddressBuilder.of(countryCode).firstName("FN").lastName("LN").streetName("sname").streetNumber("1").postalCode("12345").city("city").build();

        List<LineItemDraft> lineItemDrafts = new ArrayList<>();
        for (int i = 0; i < productNumber; i++) {
            ProductProjection product = getProduct(client, Long.valueOf(i));
            LineItemDraft lineItemDraft = LineItemDraft.of(product, product.getMasterVariant().getId(), new Random().nextInt(10) + 1);
            lineItemDrafts.add(lineItemDraft);
        }

        CartDraft cartDraft = CartDraftBuilder.of(DefaultCurrencyUnits.EUR)
                .country(countryCode)
                .locale(Locale.GERMAN)
                .lineItems(lineItemDrafts)
                .billingAddress(address).shippingAddress(address)
                .build();

        return client.executeBlocking(CartCreateCommand.of(cartDraft));
    }



    /**
     * Creates a new cart and adds the passed product as a new line item using a random quantiy between 1 and 10.
     *
     * @param client the client handling the connection
     * @return the created cart
     */
    public static Cart createTestCartFromProduct(BlockingSphereClient client, Integer productNumber) throws ExecutionException, InterruptedException {
       return  createTestCartFromProduct(CountryCode.DE, client,productNumber);
    }

    public static void removeCart(BlockingSphereClient client, Cart cart) throws ExecutionException, InterruptedException {
        if (client != null && cart != null) {
            Cart toDelete = client.executeBlocking(CartByIdGet.of(cart.getId()));
            CartDeleteCommand cartDeleteCommand = CartDeleteCommand.of(toDelete);
            client.executeBlocking(cartDeleteCommand);
        }
    }

    public static Cart updateCart(BlockingSphereClient client, Cart cart) throws ExecutionException, InterruptedException {
        CartByIdGet cartGet = CartByIdGet.of(cart.getId());
        cartGet = cartGet.plusExpansionPaths(m -> m.paymentInfo().payments());
        return client.executeBlocking(cartGet);
    }
}
