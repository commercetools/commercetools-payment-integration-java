Payment
==================

[![Build Status](https://travis-ci.org/commercetools/commercetools-sunrise-java-payment.png?branch=master)](https://travis-ci.org/commercetools/commercetools-sunrise-java-payment) [![Stories in Ready](https://badge.waffle.io/commercetools/commercetools-sunrise-java-payment.png?label=ready&title=Ready)](https://waffle.io/commercetools/commercetools-sunrise-java-payment)

Module for [Sunrise Java](https://github.com/sphereio/commercetools-sunrise-java) with different supported services to handle payment transactions. 

* [Javadoc](https://commercetools.github.io/commercetools-sunrise-java-payment/javadoc/index.html)

## General Info
The commerectools-sunrise-java-payment project intend is to make payment integration easy.

The checkout process in the shop can follow a standardized process via this module, even if different Payment Service Providers are used.
`At the moment only PayOne is supported`

The shop has to provide a sphere client for all action this module

## Compiling
Create **JAR** files:
`sbt package`

## Integration into the shop:
Include the **JAR** files to the classpath of the shop. For the play framework that is done by putting them into the **lib** folder.
In the future they should be available in a maven repository.


### Getting the payment methods 
Get all payment methods:

    final List<PaymentMethodInfo> = paymentAdapterService.findAvailablePaymentMethods();
Get filtered payment methods: (Example to get Free and only Free if *TotalPrice* is zero):

    final Function<List<PaymentMethodInfo>,List<PaymentMethodInfo> filter =
        list -> {
            if (cart.getTotalPrice().isZero()) {
                return list.stream().filter(pmi -> "FREE".equals(pmi.getMethod())).collect(Collectors.toList());
            } else {
                return list.stream().filter(pmi -> !"FREE".equals(pmi.getMethod())).collect(Collectors.toList());
            }
        }
    final List<PaymentMethodInfo> = paymentAdapterService.findAvailablePaymentMethods(filter);

### Creating the Payment 
When the customer selected a payment method that she want to use, then the shop has to call the *createPayment* method with a [CreatePaymentData](https://commercetools.github.io/commercetools-sunrise-java-payment/javadoc/com/commercetools/sunrise/payment/model/CreatePaymentData.html) object as parameter.

Different payment methods require different additional values (i.e. successUrl etc.)

Example:

    private static final String REDIRECT_URL = "redirectUrl";
    private static final String SUCCESS_URL = "successUrl";
    private static final String CANCEL_URL = "cancelUrl";
    private SphereClient sphereClient;
    
    
    paymentAdapterService.createPayment(
        CreatePaymentDataBuilder.of(sphereClient, selectedPaymentMethod, cart, orderNumber)
            .configValue(SUCCESS_URL, calculateSuccessURL(cart))
            .configValue(ERROR_URL, calculateErrorURL(cart))
            .configValue(CANCEL_URL, calculateCancelURL(cart))


### Creating the PaymentTransaction
When the customer clicks "buy" then the shop has to call the *createPaymentTransaction* method.


## Supported payment methods
Following payment methods are supported

| payment provider | payment method                     | explanation             | required parameters                    |
|------------------|------------------------------------|-------------------------|----------------------------------------|
| PAYONE           | CREDIT_CARD                        | Creditcard              | 3x URLs PseudoPan & TrunctatedcardPan  |
| PAYONE           | WALLET-PAYPAL                      | Paypal                  | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-SOFORTUEBERWEISUNG   | Sofortüberweisung       | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-POSTFINANCE_EFINANCE | Postfinance E-Finance   | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-POSTFINANCE_CARD     | Postfinance Card        | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-ADVANCE              | Prepayment              | -------                                |
| INTERNAL         | FREE                               | for zero money payments | -------                                |