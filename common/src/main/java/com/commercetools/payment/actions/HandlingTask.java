package com.commercetools.payment.actions;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Describes the next action the shop has to do after the call has finished.
 * Created by mgatz on 7/20/16.
 */
public class HandlingTask {
    private ShopAction action;
    @Nullable
    private String redirectUrl;
    private Map<String, Object> additionalData = new HashMap<>();

    private HandlingTask(ShopAction action) {
        this.action = action;
    }

    /**
     * Create a new {@link HandlingTask} instance representing the passed action.
     * @param action the {@link ShopAction} to be done
     * @return new instance of {@link HandlingTask}
     */
    public static HandlingTask of(ShopAction action) {
        return new HandlingTask(action);
    }

    /**
     * Add a redirect URL to this instance
     * @param redirectUrl the URL
     * @return enriched self
     */
    public HandlingTask redirectUrl (String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    /**
     * Add data to this handling tasks additionalData field.
     * @param key the identifier of the data object
     * @param value the object to be stored
     * @return enriched self
     */
    public HandlingTask addData(String key, Object value) {
        this.additionalData.put(key, value);
        return this;
    }

    /**
     * Provides the action type of the task.
     * @return action type
     */
    public ShopAction getAction() {

        return action;
    }

    /**
     * Provides a redirect URL of the task requires one.
     * @return the redirect URL
     */
    public Optional<String> getRedirectUrl() {

        return Optional.ofNullable(redirectUrl);
    }

    /**
     * Provides a key value store that provides more information possibly needed for the task.
     * @return value is guaranteed but map could be empty
     */
    public Map<String, Object> getAdditionalData() {

        return additionalData;
    }
}
