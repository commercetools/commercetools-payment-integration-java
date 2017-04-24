Build and publish workflow
===========================

Main project target is to release the artifacts to one of central dependencies repository 
(Maven central so far, but might be Bintray in the future). Unfortunately there is no so far any consistent way to 
publish the project using TravisCI, so manual publishing is required.

Table of content:

  - [Integration tests](#integration-tests)
  - [Publish workflow](#publish-workflow)
    - [Publish to local maven repo](#publish-to-local-maven-repo)
    - [Publish to Maven](#publish-to-maven)
      - [Signing up the app with PGP key](#2-signing-up-the-app-with-pgp-key)
      - [Deploy to OSS Sonatype](#3-deploy-to-oss-sonatype)
      - [Manually release from Sonatype web page to Maven Central](#4-manually-release-from-sonatype-web-page-to-maven-central)
      - [Publish to repo.ci.cloud.commercetools.de](#5-publish-to-repocicloudcommercetoolsde)
  - [All in one publish script](#all-in-one-publish-script)
  - [Known issues](#known-issues)
  
# Integration tests
 
For successful integration test the next settings are required:
 - **Mandatory environment variables** (see `ItConfig.java`):
 
    |  Key name                 | commercetools test environment                                                    |
    |---------------------------|-----------------------------------------------------------------------------------|
    | CT_PROJECT_KEY            | project-payment-21                                                                |
    | CT_CLIENT_ID              | see admin Sphere API Settings for project-payment-21                              |
    | CT_CLIENT_SECRET          | see admin Sphere API Settings for project-payment-21                              |
    | CT_PAYONE_INTEGRATION_URL | `https://ct-payone-integration-test.herokuapp.com/commercetools/handle/payments/` |
    
  - *CT_PAYONE_INTEGRATION_URL* resource with deployed 
    [commercetools payone integration service](https://github.com/commercetools/commercetools-payone-integration).
    This service must be connected to the same project (*CT_PROJECT_KEY*).
    - For current workflow the service is deployed to [Heroku](https://dashboard.heroku.com/apps/ct-payone-integration-test/settings)
    using *heroku cli*. To avoid any side-effect of previous tests it is recommended to re-start the service 
    before the build (this will re-initialize all custom types, see [IntegrationService#start()](https://github.com/commercetools/commercetools-payone-integration/blob/927adfa637918c20feb03242242f9d57f5561669/service/src/main/java/com/commercetools/pspadapter/payone/IntegrationService.java#L52)):
      ```
      heroku restart --app ct-payone-integration-test
      ```
      This step requires local [installed and logged in heroku CLI](https://devcenter.heroku.com/articles/heroku-command-line).
      Alternatively for remote (travis) build `$HEROKU_API_KEY` environment variable may be used instead of `heroku login`. 

If you have all above - run the tests (both unit and integration):
```
heroku restart --app ct-payone-integration-test # may be skipped on secondary run
sbt clean test it:test
```

The tests some times fail because of this [known issue](#known-issues).

# Publish workflow

## Publish to local maven repo
 
This step may be used local tests of SNAPSHOT version:
```
sbt clean test it:test publishM2
```

## Publish to Maven

### **Note**: set proper version in `version.sbt` before publishing to central repo.

If you are a new developer in the project - update contributors list in `build.sbt -> commonSettings -> pomExtra`.

The target repo is defined by _nexusHost_ variable in [build.sbt](build.sbt)

If you want to publish just test internal (SNAPSHOT) version - better to publish to 
http://repo.ci.cloud.commercetools.de/ first ([see the instructions below](#5-publish-to-repo.ci.cloud.commercetools.de)),
and then if the version is ready to be public released - publish to Maven Central.

Publishing to Maven Central requires the next steps:

 1. Build the app (see the steps above for integration tests)
 2. [Signing up the app with PGP key](#2-signing-up-the-app-with-pgp-key)
 3. [Deploy to OSS Sonatype](#3-deploy-to-oss-sonatype)
 4. [Manually release from Sonatype web page to Maven Central](#4-manually-release-from-sonatype-web-page-to-maven-central)
 
**Note**: to publish only to Sonatype (both OSS or commercetools) PGP signing is not required.
 
### 2. Signing up the app with PGP key

This step requires installed [PGP tool](https://gpgtools.org/), 
[published PGP key](http://security.stackexchange.com/questions/406/how-should-i-distribute-my-public-key) and 
[configured SBT PGP plugin](http://www.scala-sbt.org/sbt-pgp/). 

More info about SBT publishing find here:

http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html

http://www.scala-sbt.org/0.12.4/docs/Detailed-Topics/Publishing.html

### 3. Deploy to OSS Sonatype

Ensure _nexusHost_ variable in [build.sbt](build.sbt) is "oss.sonatype.org".

Current `sbt` build script expects to find Sonatype login/password in environment variables `$NEXUS_USER`/ `$NEXUS_PASS`.
Ensure the variables values are adjusted with _nexusHost_ value. Please ask OPS to get the credentials for these enviroment variables.

For simple publishing to Sonatype `sbt publish` may be used, but in this case it will not be able to deploy to 
Maven Central. To publish signed app use `sbt publish-signed`:
```
heroku restart --app ct-payone-integration-test # may be skipped on secondary run
sbt clean test it:test publish-signed
```

This process will ask passphrase for you PGP private key before signing the application.

The script will publish to Sonatype `repositories/snapshots/` or `repositories/releases/` based on version value 
(from [version.sbt](/version.sbt))

See http://central.sonatype.org/pages/sbt.html for more details of Sonatype sbt build.

### 4. Manually release from Sonatype web page to Maven Central

Log into [OSSRH](https://oss.sonatype.org/), find your
[Staging Repository](http://central.sonatype.org/pages/releasing-the-deployment.html#locate-and-examine-your-staging-repository),
select the staging repository and the panel below the list will display further details about the repository. 
Press _Close_ button on the top panel. This will trigger the evaluations of the components against the 
[requirements](http://central.sonatype.org/pages/requirements.html). 
Once you have successfully closed the staging repository, you can release it by pressing the _Release_ button next to 
previously clicked _Close_ button. This will move the components into the release repository of OSSRH where 
it will be synced to the Central Repository. Note: syncing project may take some time, so it will not appear instantly
in the Maven Repo.

As soon as artifacts are synced you will be able to find them in the Maven Central repo and mirrors:

https://repo1.maven.org/maven2/com/commercetools/sunrise/payment/

For more details about the release workflow see:

 - http://central.sonatype.org/pages/ossrh-guide.html
 - http://central.sonatype.org/pages/releasing-the-deployment.html
 - http://central.sonatype.org/pages/sbt.html
 - https://www.youtube.com/watch?v=b5D2EBjLp40 and https://www.youtube.com/watch?v=dXR4pJ_zS-0
 
### 5. Publish to repo.ci.cloud.commercetools.de

Same as steps 1 - 3 above, but ensure _nexusHost_ variable in [build.sbt](build.sbt) is "repo.ci.cloud.commercetools.de" 
and `$NEXUS_USER`/ `$NEXUS_PASS` values are adjusted with _nexusHost_ value.

Signing in the release is not required, but encouraged.

# All in one publish script

As a summary of all the steps above you may use _all-in-one_ publish script from the root of the repo:

```
./publish.sh
```

This scripts restarts Heroku integration test environment, builds the app, runs unit and integration tests, sings the
artifacts and deploys them to Sonatype. To execute the script successfully you should have all the settings above, 
namely:

 - installed and logged in heroku cli (or `$HEROKU_API_KEY` environment variable set instead of log in)
 - exported environment variables for integrations tests and Sonatype login
 - installed pgp client and published PGP key 

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
 
