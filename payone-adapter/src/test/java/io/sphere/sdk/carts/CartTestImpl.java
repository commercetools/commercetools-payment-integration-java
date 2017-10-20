package io.sphere.sdk.carts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.cartdiscounts.CartDiscount;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.discountcodes.DiscountCodeInfo;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.types.CustomFields;

import javax.annotation.Nullable;
import javax.money.MonetaryAmount;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

/**
 * <i>KISS</i> implementation of {@link Cart} interface for tests purpose:
 * it has public properties for all the interface methods, so may be easily changed during the tests.
 * <p>
 *     <b>Developers</b>: the implementation is not completely finished, so add required getters' properties
 *     when you need them.
 * </p>
 */
public class CartTestImpl implements Cart {

    // Dummy Interface implementation. Look for related properties at the bottom of the class.

    @Override
    public CartState getCartState() {
        return null;
    }

    @Override
    public InventoryMode getInventoryMode() {
        return null;
    }

    @Nullable
    @Override
    public CartShippingInfo getShippingInfo() {
        return null;
    }

    @Nullable
    @Override
    public Address getBillingAddress() {
        return null;
    }

    @Nullable
    @Override
    public CountryCode getCountry() {
        return null;
    }

    @Nullable
    @Override
    public String getCustomerEmail() {
        return null;
    }

    @Nullable
    @Override
    public Reference<CustomerGroup> getCustomerGroup() {
        return null;
    }

    @Nullable
    @Override
    public String getCustomerId() {
        return null;
    }

    @Override
    public List<CustomLineItem> getCustomLineItems() {
        return null;
    }

    @Override
    public List<LineItem> getLineItems() {
        return null;
    }

    @Nullable
    @Override
    public Address getShippingAddress() {
        return null;
    }

    @Nullable
    @Override
    public TaxedPrice getTaxedPrice() {
        return null;
    }

    @Override
    public MonetaryAmount getTotalPrice() {
        return totalPrice;
    }

    @Override
    public List<DiscountCodeInfo> getDiscountCodes() {
        return null;
    }

    @Nullable
    @Override
    public CustomFields getCustom() {
        return null;
    }

    @Nullable
    @Override
    public PaymentInfo getPaymentInfo() {
        return null;
    }

    @Override
    public TaxMode getTaxMode() {
        return null;
    }

    @Nullable
    @Override
    public String getAnonymousId() {
        return null;
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public Long getVersion() {
        return null;
    }

    @Override
    public ZonedDateTime getCreatedAt() {
        return null;
    }

    @Override
    public ZonedDateTime getLastModifiedAt() {
        return null;
    }

    @Nullable
    @Override
    public Integer getDeleteDaysAfterLastModification() {
        return null;
    }

    @Override
    public RoundingMode getTaxRoundingMode() {
        return null;
    }

    @Nullable
    @Override
    public List<Reference<CartDiscount>> getRefusedGifts() {
        return null;
    }

// Put the properties for the getters here
    // we keep them public for easy mocking

    public MonetaryAmount totalPrice;
    public Locale locale;
}
