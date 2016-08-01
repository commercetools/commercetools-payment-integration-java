package com.commercetools.sunrise.payment.actions;

/**
 * Describes what kind of action the shop has to do next.
 * Created by mgatz on 7/20/16.
 */
public enum  ShopAction {
    /**
     * Nothing payment specific to execute left. Go one processing.
     */
    CONTINUE,
    /**
     * Shop needs to redirect the user immediately to the provided URL and handle its callbacks.
     */
    REDIRECT_BEFORE_CHECKOUT,
    /**
     * Shop needs to redirect the user after a successful checkout to the provided URL and handle its callbacks.
     */
    REDIRECT_AFTER_CHECKOUT,
    /**
     * Shop needs to handle an occurred error.
     */
    HANDLE_ERROR,
    /**
     * Something not generic is required. It is the task of the shop to decide what and how that has to be done.
     * Simple continuation is not possible.
     */
    CUSTOM
}