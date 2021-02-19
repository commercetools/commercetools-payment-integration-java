Build and publish workflow
===========================

The main goal of the build process is to publish the artifacts to public repositories, 
like [Maven Central](https://search.maven.org/).

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Tablex of Contents** 

- [Build and deploy a new version](#build-and-deploy-a-new-version)
- [Integration tests](#integration-tests)
- [Publish workflow](#publish-workflow)
  - [Full build with tests and documentation publishing](#full-build-with-tests-and-documentation-publishing)
  - [Publish to local maven repo](#publish-to-local-maven-repo)
  - [Release the library](#release-the-library)
    - [Publish to Maven central](#publish-to-maven-central)
      - [Login into nexus repository manager](#login-into-nexus-repository-manager)
      - [Locate and examine your staging repository](#locate-and-examine-your-staging-repository)
      - [Close and drop or release your staging repository](#close-and-drop-or-release-your-staging-repository)
- [All in one publish script](#all-in-one-publish-script)
- [Known issues](#known-issues)
    - [`PayonePrepaidTest.testPaymentFlow`](#payoneprepaidtesttestpaymentflow)
    - [Any test which makes requests to commercetools platform environment may fail with:](#any-test-which-makes-requests-to-commercetools-platform-environment-may-fail-with)
    - [Aggregated Javadoc may fail without visible reason](#aggregated-javadoc-may-fail-without-visible-reason)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

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
  - `BINTRAY_USER` and `BINTRAY_KEY` environment variables to publish the artifacts to Bintray 

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

# Publish workflow

## Full build with tests and documentation publishing

Replace `X.X.X` in the snippet:

```
./gradlew clean build aggregateJavaDoc gitPublishPush -Dbuild.version=X.X.X
```

## Publish to local maven repo
 
This step may be used for local test versions:
```
./gradlew clean install -Dbuild.version=X.X.X
```

If you want to review full generated `pom.xml` (with license, scm, developers) like it will be published, then use:
```
./gradlew clean publishToMavenLocal -Dbuild.version=X.X.X
```

where `publishToMavenLocal` is a task from 
[`maven-publish`](https://docs.gradle.org/3.3/userguide/publishing_maven.html#publishing_maven:install)
plugin.

Use [publish-to-maven-local.sh](./publish-to-maven-local.sh) script for easier publishing process.

##  Release the library
To release the library, you need to ["create a new release"](https://github.com/commercetools/commercetools-payment-integration-java/releases/new) with Github, 
describe the new release and publish it. 
The creation of a github release triggers a github action, which will deploy the library to  the staging repository
 of the [nexus repository manager](https://oss.sonatype.org/).

###  Publish to Maven central

#### Login into nexus repository manager
You need to login to [nexus repository manager](https://oss.sonatype.org/) in order to access and work with your staging repositories. 

#### Locate and examine your staging repository
Once you are logged in you will be able to access the build promotion menu in the left hand navigation and select the
staging repository. The staging repository you created during the deployment will have a name starting with the
groupId for your projects with the dots removed appended with a dash and a 4 digit number. E.g. if your project groupId
is com.example.applications, your staging profile name would start with comexampleapplications. The sequential numbers 
start at 1000 and are incremented per deployment so you could e.g. have a staging repository name of comexampleapplication-1010.

Select the staging repository and the panel below the list will display further details about the repository.

#### Close and drop or release your staging repository
After your deployment the repository will be in an open status. You can evaluate the deployed components in the
repository using the Contents tab. If you believe everything is correct, you can press the close button above the list.
This will trigger the evaluations of the components against the requirements.Once you have successfully closed the staging repository, you can release it by pressing the Release button. 
This will move the components into the release repository of [nexus repository manager](https://oss.sonatype.org/)  where it will be synced to the Central Repository.


# All in one publish script

As a summary of all the steps above you may use _all-in-one_ publish script from the root of the repo:

```
./publish.sh X.X.X
```
where `X.X.X` is a new deploy version.

This scripts restarts Heroku integration test environment, builds the app, runs unit and integration tests, 
aggregates and deploys to Github all javadoc, uploads all artifacts to Bintray. 
To execute the script successfully you should have all the settings above, namely:

 - installed and logged in heroku cli (or `$HEROKU_API_KEY` environment variable set instead of log in)
 - exported `GRGIT_USER` github token to publish the documentation
 - exported `BINTRAY_USER` and `BINTRAY_KEY` environment variables.

# Known issues
  ### [`PayonePrepaidTest.testPaymentFlow`](https://github.com/commercetools/commercetools-payment-integration-java/blob/master/src/it/com/commercetools/payment/PayonePrepaidTest.java)
  and [`PayonePaypalTest.testPaymentFlow`](https://github.com/commercetools/commercetools-payment-integration-java/blob/master/src/it/com/commercetools/payment/PayonePaypalTest.java)
 sometimes fail with error:
 > _expected:<[SUCCESS]> but was:<[FAILED]>_. 
 
 in asserts of `assertPaymentTransactionObjectCreation():`
 > assertThat(ptcr.getOperationResult()).isEqualTo(OperationResult.SUCCESS);
 
  
 Still not clear why, but should be investigated.
 It might be connected to parallel execution, but likely not.
 
 ### Any test which makes requests to commercetools platform environment may fail with:
 > java.util.concurrent.ExecutionException: io.sphere.sdk.http.HttpException: 
 > The underlying HTTP client detected a problem.
 
 ### Aggregated Javadoc may fail without visible reason
  
  The used `gradle-aggregate-javadocs-plugin` has know issue of bad errors reporting: 
  it fails without explicitly pointing the reason, something like this:
  
  ```
  gradle Javadoc generation failed. Generated Javadoc options file (useful for troubleshooting)... 
  See generated javadoc.options
  ```
  
  To find the real issue run default gradle `javadoc` task like this:
  
  ```
    ./gradlew clean javadoc
  ```
  and this will point you the problematic files/docs. 
