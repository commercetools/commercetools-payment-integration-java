Build and publish workflow
===========================

The main goal of the build process is to publish the artifacts to public repositories, 
like [Sonatype](https://oss.sonatype.org/) and [Maven Central](https://search.maven.org/).

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents** 

- [Build and deploy a new version](#build-and-deploy-a-new-version)
  - [Release library in Github](#release-library-in-github)
  - [Final Step](#final-step)
- [Integration tests](#integration-tests)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Build and deploy a new version

## Release library in Github   
To release the library, you need to ["create a new release"](https://github.com/commercetools/commercetools-payment-integration-java/releases/new) with Github, 
describe the new release (define important changes, breaking changes or important new features) and publish it. 

The creation of a github release triggers a [github action](https://github.com/commercetools/commercetools-payment-integration-java/actions/workflows/cd.yml), 
which will deploy the library first to [Sonatype staging repo](https://oss.sonatype.org) and then to [Maven Central](https://mvnrepository.com/artifact/com.commercetools.payment/nopsp-adapter).

## Final Step
After the release build status is **success** ensure that the new version is publicly available at [Maven Central](https://mvnrepository.com/artifact/com.commercetools.payment/nopsp-adapter). 


# Integration tests
 
For successful integration test on remote machine (e.g. Travis) following settings are required:
 - **Mandatory CTP environment variables** (see [`ItConfig.java`](/blob/master/src/it/com/commercetools/config/ItConfig.java)):
 
    |  Key name                 | commercetools test environment                                                                    |
    |---------------------------|---------------------------------------------------------------------------------------------------|
    | CT_PROJECT_KEY            | project-payment-21                                                                                |
    | CT_PROJECT_KEY            | project-payment-21                                                                                |
    | CT_CLIENT_ID              | see [CTP API Settings for project-payment-21](https://admin.commercetools.com/project-payment-21) |
    | CT_CLIENT_SECRET          | see [CTP API Settings for project-payment-21](https://admin.commercetools.com/project-payment-21) |
    | CT_PAYONE_INTEGRATION_URL | `https://ct-payment-integration-java.herokuapp.com/commercetools/handle/payments/`                |
    
  - `CT_PAYONE_INTEGRATION_URL` resource with deployed 
    [commercetools payone integration service](https://github.com/commercetools/commercetools-payone-integration).
    This service must be connected to the same project (*CT_PROJECT_KEY*).
    - For current workflow the service might be deployed to [Heroku](https://dashboard.heroku.com/apps/ct-payment-integration-java/settings)
    using [*heroku cli*](https://devcenter.heroku.com/articles/heroku-cli) or direct push to heroku repository from 
    [payone-integration-service](https://github.com/commercetools/commercetools-payone-integration):
    ```
    git clone git@github.com:commercetools/commercetools-payone-integration.git
    cd commercetools-payone-integration
    git push https://git.heroku.com/ct-payment-integration-java.git master
    ```
      For more information about how to deploy the service see [Heroku Deployment](https://devcenter.heroku.com/categories/deployment) and 
      [Payone integration service build](https://github.com/commercetools/commercetools-payone-integration#build)
    
    - To avoid any side-effect of previous tests it is recommended to re-start the service 
    before the build (this will re-initialize all custom types, see [IntegrationService#start()](https://github.com/commercetools/commercetools-payone-integration/blob/927adfa637918c20feb03242242f9d57f5561669/service/src/main/java/com/commercetools/pspadapter/payone/IntegrationService.java#L52)):
      ```
      heroku restart --app ct-payment-integration-java
      ```
      This step requires local [installed and logged in heroku CLI](https://devcenter.heroku.com/articles/heroku-command-line).
      Alternatively for remote (travis) build `$HEROKU_API_KEY` environment variable may be used instead of `heroku login`.
       
  - `GRGIT_USER` environment variable (github token) to publish aggregated Javadoc

If you have all above - build and run the tests (both unit and integration):
```
heroku restart --app ct-payment-integration-java # may be skipped on secondary run
./gradlew clean build
```

To run all these tests locally (including IDE) with mandatory properties above - use `it.properties` file 
(in `it/resources` directory) with all secret values. See [`ItConfig.java`](/blob/master/src/it/com/commercetools/config/ItConfig.java) for more details. 
Also you could copy and edit [`it/resources/it.properties.skeleton`](/blob/master/src/it/resources/it.properties.skeleton).

The gradle _build_ task is configured to be depended on all the tests, including Unit tests on all sub-projects and 
common integration tests from [`src/`](/src/) directory.

Because of the [known issues](#known-issues) some tests may fail.

