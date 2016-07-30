package com.commercetools.sunrise.payment;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartDeleteCommand;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.client.SphereClient;
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

/**
 * Created by mgatz on 7/10/16.
 */
public class IntegrationTestUtils {
    /**
     * @return a newly created test sphere client
     */
    public static SphereClient createClient() {
        String projectKey = System.getenv("CTP_PROJECT_KEY");
        String clientSecret = System.getenv("CTP_CLIENT_SECRET");
        String clientId = System.getenv("CTP_CLIENT_ID");
        return SphereClientFactory.of().createClient(projectKey, clientId, clientSecret);
    }

    /**
     * Returns the first product found inside the client connected project.
     * Executes blocking.
     * @param client the client handling the connection
     * @return the product projection
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static ProductProjection getProduct(SphereClient client) throws ExecutionException, InterruptedException {
        ProductProjectionQuery query = ProductProjectionQuery.ofCurrent().withLimit(1);
        return client.execute(query).toCompletableFuture().get().getResults().get(0);
    }

    /**
     * Creates a new cart and adds the passed product as a new line item using a random quantiy between 1 and 10.
     * @param client the client handling the connection
     * @param product the product used as line item
     * @return the created cart
     */
    public static Cart createTestCartFromProduct(SphereClient client, ProductProjection product) throws ExecutionException, InterruptedException {
        Address address = AddressBuilder.of(CountryCode.DE).firstName("FN").lastName("LN").streetName("sname").streetNumber("1").postalCode("12345").city("city").build();


        LineItemDraft lineItemDraft = LineItemDraft.of(product, product.getMasterVariant().getId(), new Random().nextInt(10) + 1);
        List<LineItemDraft> lineItemDrafts = new ArrayList<>();
        lineItemDrafts.add(lineItemDraft);

        CartDraft cartDraft = CartDraftBuilder.of(DefaultCurrencyUnits.EUR)
                .country(CountryCode.DE)
                .locale(Locale.GERMAN)
                .lineItems(lineItemDrafts)
                .billingAddress(address).shippingAddress(address)
                .build();

        return client.execute(CartCreateCommand.of(cartDraft)).toCompletableFuture().get();
    }

    public static void removeCart(SphereClient client, Cart cart) throws ExecutionException, InterruptedException {
        Cart toDelete = client.execute(CartByIdGet.of(cart.getId())).toCompletableFuture().get();
        CartDeleteCommand cartDeleteCommand = CartDeleteCommand.of(toDelete);
        client.execute(cartDeleteCommand).toCompletableFuture().get();
    }
}
