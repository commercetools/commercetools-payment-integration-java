Payment
==================

[![Build Status](https://travis-ci.com/commercetools/project-payment.png?branch=master)](https://travis-ci.com/commercetools/project-payment) 
[![Stories in Ready](https://badge.waffle.io/commercetools/project-payment.png?label=ready&title=Ready)](https://waffle.io/commercetools/project-payment)

Module for [Sunrise Java](https://github.com/sphereio/commercetools-sunrise-java) with different supported services to handle payment transactions. 

* [Javadoc](https://commercetools.github.io/project-payment/javadoc/index.html)

## General Info
The project-payment project intend is to make payment integration easy.

The checkout process in the shop can follow a standardized process via this module, even if different Payment Service Providers are used.
`At the moment only PayOne is supported`

The shop has to provide a sphere client for all action this module

## Compiling
Create **JAR** files:
`sbt package`

Create **JAR** and run tests:
`sbt clean package test`

## Integration into the shop:
Take the dependencies from Maven central.

### Maven example

```
<dependency>
  <groupId>com.commercetools.sunrise.payment</groupId>
  <artifactId>payone-adapter</artifactId>
  <version>0.1</version>
</dependency>
<dependency>
  <groupId>com.commercetools.sunrise.payment</groupId>
  <artifactId>common</artifactId>
  <version>0.1</version>
</dependency>
```

### SBT example

```
libraryDependencies ++= Seq(
  "com.commercetools.sunrise.payment" % "common" % "0.1",
  "com.commercetools.sunrise.payment" % "payone-adapter" % "0.1",
)
```

### Gradle example

```
repositories {
  mavenCentral()
}

dependencies {
    compile "com.commercetools.sunrise.payment:common:0.1"
    compile "com.commercetools.sunrise.payment:payone-adapter:0.1"
}
```

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
When the customer selected a payment method that she want to use, then the shop has to call the *createPayment* method with a 
[CreatePaymentData](https://commercetools.github.io/project-payment/javadoc/com/commercetools/sunrise/payment/model/CreatePaymentData.html) object as parameter.

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