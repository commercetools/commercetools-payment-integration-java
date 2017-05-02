<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Payment](#payment)
  - [General Info](#general-info)
  - [Compiling](#compiling)
  - [Integration into the shop:](#integration-into-the-shop)
    - [Maven example](#maven-example)
    - [SBT example](#sbt-example)
    - [Gradle example](#gradle-example)
    - [Getting the payment methods](#getting-the-payment-methods)
    - [Creating the Payment](#creating-the-payment)
    - [Creating the PaymentTransaction](#creating-the-paymenttransaction)
  - [Supported payment methods](#supported-payment-methods)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

Payment
==================

[![Build Status](https://travis-ci.com/commercetools/commercetools-payment-integration-java.svg?token=xBfuKwz4mtssFw4UENNy&branch=master)](https://travis-ci.com/commercetools/project-payment) 
[![Stories in Ready](https://badge.waffle.io/commercetools/commercetools-payment-integration-java.png?label=ready&title=Ready)](https://waffle.io/commercetools/project-payment)

Java modules to create and handle payments and payment transactions in different _payment integration services (providers)_, 
like [`commercetools Payone Integration Service`](https://github.com/commercetools/commercetools-payone-integration). 

* [Javadoc](https://commercetools.github.io/commercetools-payment-integration-java/javadoc/v/)

* [Build and publish workflow](BUILD.md)

## General Info
The `commercetools-payment-integration-java` project intend is to make payment integration easy.

The checkout process in the shop can follow a standardized process via this module, even if different Payment Service Providers are used.
`At the moment only PayOne is supported`

The shop has to provide a sphere client for all action this module

## Compiling
Create **JAR** files:
`./gradlew clean jar`

Create **JAR** files and test:
`./gradlew clean check`

(**Recommended**) Full build with tests, but without install to maven local repo:
`./gradlew clean build`

Install to local maven repo:
`./gradlew clean install`

Publish to Bintray:
`./gradlew clean -Dbuild.version=$TRAVIS_TAG bintrayUpload`

For more info about build and publish process see [BUILD](BUILD.md) documentation

## Integration into the shop:
Take the dependencies from Maven central.

### Maven example

```
<dependency>
  <groupId>com.commercetools.payment</groupId>
  <artifactId>common</artifactId>
  <version>0.6.2</version>
</dependency>
<dependency>
  <groupId>com.commercetools.payment</groupId>
  <artifactId>payone-adapter</artifactId>
  <version>0.6.2</version>
</dependency>
```

### SBT example

```
libraryDependencies ++= Seq(
  "com.commercetools.payment" % "common" % "0.6.2",
  "com.commercetools.payment" % "payone-adapter" % "0.6.2",
)
```

### Gradle example

```
repositories {
  mavenCentral()
}

dependencies {
    compile "com.commercetools.payment:common:0.6.2"
    compile "com.commercetools.payment:payone-adapter:0.6.2"
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
[CreatePaymentData](https://commercetools.github.io/commercetools-payment-integration-java/javadoc/v/current/com/commercetools/payment/model/CreatePaymentData.html) object as parameter.

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
