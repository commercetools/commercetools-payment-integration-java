<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [Commercetools Payment Integration Java](#commercetools-payment-integration-java)
  - [General Info](#general-info)
  - [Build](#build)
  - [Project configuration:](#project-configuration)
    - [Maven](#maven)
    - [SBT](#sbt)
    - [Gradle](#gradle)
  - [Usage](#usage)
    - [List available payment methods](#list-available-payment-methods)
    - [Create payment object](#create-payment-object)
    - [Creating the PaymentTransaction](#creating-the-paymenttransaction)
  - [Supported payment methods](#supported-payment-methods)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

Commercetools Payment Integration Java
==================

[![Build Status](https://travis-ci.org/commercetools/commercetools-payment-integration-java.svg?branch=master)](https://travis-ci.org/commercetools/commercetools-payment-integration-java) 

Java modules to create and handle payments and payment transactions in different _payment integration services (providers)_, 
like [`commercetools Payone Integration Service`](https://github.com/commercetools/commercetools-payone-integration). 

* [Javadoc](https://commercetools.github.io/commercetools-payment-integration-java/javadoc/v/)

* [Build and publish workflow](BUILD.md)

## General Info
This projects provides collection of utils which supports payment integrations based on intermediary services like 
[Payone Integration Service](https://github.com/commercetools/commercetools-payone-integration).

The checkout process in the shop can follow a standardized process via this module, even if different Payment Service Providers are used.
`At the moment only PayOne is supported`

The shop has to provide a sphere client for all action this module

## Build
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

## Project configuration:
Take the dependencies from Maven central, Bintray/JCenter.

### Maven

  1. Add _JCenter_ and/or _Bintray_ repositories references in the project `pom.xml` or in common maven `settings.xml`.
  An Example of `pom.xml` configuration:
  ```
    <repositories>
      <repository>
        <snapshots>
          <enabled>false</enabled>
        </snapshots>
        <id>jcenter</id>
        <url>https://jcenter.bintray.com/</url>
        <name>jcenter</name>
      </repository>
      
      <repository>
        <snapshots>
          <enabled>false</enabled>
        </snapshots>
        <id>bintray-commercetools-maven</id>
        <name>bintray-commercetools-maven</name>
        <url>https://dl.bintray.com/commercetools/maven</url>
      </repository>
    </repositories>
  ```
  
  - **Note the difference**: [_JCenter_](https://bintray.com/bintray/jcenter) is a public common repository 
  (almost the same as _Maven Central_), where the artifacts are published **forever** (almost). 
  Opposite to this, [_bintray-commercetools-maven_](https://bintray.com/commercetools/maven/) 
  is a corporate account storage, where temporary artifacts for development are stored and could be removed 
  (almost like `SNAPSHOT`s in _OSS Sonatype_ repo)
  
  - See [Setting up Multiple Repositories](https://maven.apache.org/guides/mini/guide-multiple-repositories.html)
  guide for more details how to configure third party maven repositories.

  2. When the repositories are configure, add the payment dependencies:
    
  ```xml
    <dependencies>
      <dependency>
        <groupId>com.commercetools.payment</groupId>
        <artifactId>payone-adapter</artifactId>
        <version>1.0.0</version>
      </dependency>
    </dependencies>
  ```

### SBT

  1. [Configure _JCenter_ and/or _Bintray_ repositories](http://www.scala-sbt.org/0.13/docs/Resolvers.html)
  
  2. Add to `build.sbt`
  
  ```scala
    libraryDependencies ++= Seq(
      "com.commercetools.payment" % "payone-adapter" % "1.0.0"
   )
  ```

### Gradle

  1. Add dependencies repositories into the head of `build.gradle` (note, you are not obligated to add all of them):
  
  ```groovy
    repositories {
      mavenCentral()
      jcenter()
      maven {
          url  "http://dl.bintray.com/commercetools/maven" // usually used only for developers to test non-stable not published versions
      }
    }
  ```

  2. Add dependencies to project/sub-project settings in `build.gradle`:
  
  ```groovy
    dependencies {
      compile "com.commercetools.payment:payone-adapter:1.0.0"
    }
  ```
  
## Usage

### List available payment methods 
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

### Create payment object 

In order to persist selected payment method in CTP use [createPayment(CreatePaymentData data)](https://commercetools.github.io/commercetools-payment-integration-java/javadoc/v/current/com/commercetools/payment/service/PaymentAdapterService.html#createPayment-com.commercetools.payment.model.CreatePaymentData-)
method


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
| PAYONE           | WALLET-PAYDIREkT                   | Paydirekt               | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-SOFORTUEBERWEISUNG   | Sofortüberweisung       | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-POSTFINANCE_EFINANCE | Postfinance E-Finance   | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-POSTFINANCE_CARD     | Postfinance Card        | 3x URLs                                |
| PAYONE           | BANK_TRANSFER-ADVANCE              | Prepayment              | -------                                |
| INTERNAL         | FREE                               | for zero money payments | -------                                |
