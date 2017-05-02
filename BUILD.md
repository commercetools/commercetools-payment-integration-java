Build and publish workflow
===========================

Main project target is to release the artifacts to one of central dependencies repository 
(Maven central so far, but might be Bintray in the future). Unfortunately there is no so far any consistent way to 
publish the project using TravisCI, so manual publishing is required.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents** 

- [Integration tests](#integration-tests)
- [Publish workflow](#publish-workflow)
  - [Publish to local maven repo](#publish-to-local-maven-repo)
  - [Publish to Maven](#publish-to-maven)
    - [2. Signing up the app with PGP key](#2-signing-up-the-app-with-pgp-key)
    - [3. Deploy to OSS Sonatype](#3-deploy-to-oss-sonatype)
    - [4. Manually release from Sonatype web page to Maven Central](#4-manually-release-from-sonatype-web-page-to-maven-central)
    - [5. Publish to repo.ci.cloud.commercetools.de](#5-publish-to-repocicloudcommercetoolsde)
- [All in one publish script](#all-in-one-publish-script)
- [Known issues](#known-issues)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Integration tests
 
For successful integration test on remote machine (e.g. Travis) the next settings are required:
 - **Mandatory CTP environment variables** (see `ItConfig.java`):
 
    |  Key name                 | commercetools test environment                                                    |
    |---------------------------|-----------------------------------------------------------------------------------|
    | CT_PROJECT_KEY            | project-payment-21                                                                |
    | CT_CLIENT_ID              | see admin Sphere API Settings for project-payment-21                              |
    | CT_CLIENT_SECRET          | see admin Sphere API Settings for project-payment-21                              |
    | CT_PAYONE_INTEGRATION_URL | `https://ct-payment-integration-java.herokuapp.com/commercetools/handle/payments/` |
    
  - `CT_PAYONE_INTEGRATION_URL` resource with deployed 
    [commercetools payone integration service](https://github.com/commercetools/commercetools-payone-integration).
    This service must be connected to the same project (*CT_PROJECT_KEY*).
    - For current workflow the service is deployed to [Heroku](https://dashboard.heroku.com/apps/ct-payment-integration-java/settings)
    using *heroku cli*. To avoid any side-effect of previous tests it is recommended to re-start the service 
    before the build (this will re-initialize all custom types, see [IntegrationService#start()](https://github.com/commercetools/commercetools-payone-integration/blob/927adfa637918c20feb03242242f9d57f5561669/service/src/main/java/com/commercetools/pspadapter/payone/IntegrationService.java#L52)):
      ```
      heroku restart --app ct-payment-integration-java
      ```
      This step requires local [installed and logged in heroku CLI](https://devcenter.heroku.com/articles/heroku-command-line).
      Alternatively for remote (travis) build `$HEROKU_API_KEY` environment variable may be used instead of `heroku login`.
       
  - `GRGIT_USER` environment variable (github token) to publish aggregated Javadoc
  - `BINTRAY_USER` and `BINTRAY_KEY` environment variables to publish the artifacts to Bintray 

If you have all above - build and run the tests (both unit and integration):
```
heroku restart --app ct-payment-integration-java # may be skipped on secondary run
./gradlew clean build
```

The gradle _build_ task is configured to be depended on all the tests, including Unit tests on all sub-projects and 
common integration tests from [`src/`](/src/) directory.

The tests some times fail because of this [known issues](#known-issues).

# Publish workflow

## Full build with tests, documentation publishing and Bintray upload

Replace `X.X.X` in the snippet:

```
./gradlew clean build aggregateJavaDoc gitPublishPush bintrayUpload -Dbuild.version=X.X.X
```

## Publish to local maven repo
 
This step may be used for local test versions:
```
./gradlew clean build publishPaymentPublicationPublicationToMavenLocal
```

## Publish to Bintray

[Bintray documentation about publish process](https://blog.bintray.com/2014/02/11/bintray-as-pain-free-gateway-to-maven-central/)

Bintray publish is performed by `gradle-bintray-plugin`. The artifacts are published to 
https://bintray.com/commercetools/maven/payment 
repo.

If you are a new developer in the project - update contributors list in 
[`build.gradle`](/build.gradle)`-> subprojects -> pomConfig ->developers`.

To initiate publish call:
```
./gradlew clean build bintrayUpload -Dbuild.version=<<NEW_VERSION>>
```

**NOTE**: Bintray does not allow to publish snapshots thus `<<NEW_VERSION>>` should not contation _SNAPSHOT_.
If you wish to use snapshots, https://oss.jfrog.com account should be configured.
See https://blog.bintray.com/2014/02/11/bintray-as-pain-free-gateway-to-maven-central/ for more info.

Instead of snapshots we recommend to use _alpha_, _beta_ etc version suffixes.

When the publish done the artifacts are available in [Bintray Download](http://dl.bintray.com/commercetools/maven/com/commercetools/payment/),
but still not available in [JCenter](https://jcenter.bintray.com/com/commercetools/payment/). 

To publish the artifacts to JCenter do the next:
  1. Go to https://bintray.com/commercetools/maven/payment
  1. You will see something like _Notice: You have 24 unpublished item(s) for this package (expiring in 6 days and 22 hours)_
  1. Click _Publish_
  1. Check [JCenter Payment](https://jcenter.bintray.com/com/commercetools/payment/) reference.

## Publish to Maven

Publishing to Maven Central requires the next steps:

 1. Build the app and upload to Bintray (see the steps above for integration tests)
 1. [Signing up the app with PGP key](https://blog.bintray.com/2013/08/06/fight-crime-with-gpg/): for now we use Bintray's 
    "a stock built-in key-pair so that it can auto-sign every file you upload"
 1. [Manually release from Bintray web page to Maven Central](https://blog.bintray.com/2015/09/17/publishing-your-maven-project-to-bintray/)
 
**Note**: Maven Central has much stricter requirements to published artifacts, e.g. the should have mandatory POM fields 
(like developers list, SCM references; this is configured in the [build script](/build.gradle)) 
and mandatory signed by GPG key (could be performed by Bintray settings). For more info about Maven Central 
requirements see [Requirements](http://central.sonatype.org/pages/requirements.html) page.

As soon as artifacts are synced you will be able to find them in the Maven Central repo and mirrors:

https://repo1.maven.org/maven2/com/commercetools/payment/
http://repo2.maven.org/maven2/com/commercetools/payment/

# All in one publish script

As a summary of all the steps above you may use _all-in-one_ publish script from the root of the repo:

```
./publish.sh X.X.X
```
where `X.X.X` is a new deploy version.

This scripts restarts Heroku integration test environment, builds the app, runs unit and integration tests, 
aggregates and deploys to Github all javadoc, upload all artifacts to Bintray. 
To execute the script successfully you should have all the settings above, namely:

 - installed and logged in heroku cli (or `$HEROKU_API_KEY` environment variable set instead of log in)
 - exported `GRGIT_USER` github token to publish the documentation
 - exported `BINTRAY_USER` and `BINTRAY_KEY` environment variables.

# Known issues
 1. `PayonePrepaidTest.testPaymentFlow` and `PayonePaypalTest.testPaymentFlow` 
 sometimes fail with error:
 > _expected:<[SUCCESS]> but was:<[FAILED]>_. 
 
 in asserts of `assertPaymentTransactionObjectCreation():`
 > assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
 
  
 Still not clear why, but should be investigated.
 It might be connected to parallel execution, but likely not.
 
 2. Any test which makes requests to Sphere environment may fail with:
 > java.util.concurrent.ExecutionException: io.sphere.sdk.http.HttpException: 
 > The underlying HTTP client detected a problem.
 
