Build and publish workflow
===========================

Main project target is to release the artifacts to one of central dependencies repository 
(Maven central so far, but might be Bintray in the future). Unfortunately there is no so far any consistent way to 
publish the project using TravisCI, so manual publishing is required.

## Integration tests
 
For successful integration test the next settings required:
 - **Mandatory environment variables** (see `ItConfig.java`):
 
    |  Key name                 | commercetools test environment                                                  |
    |---------------------------|---------------------------------------------------------------------------------|
    | CT_PROJECT_KEY            | project-payment-21                                                              |
    | CT_CLIENT_ID              | see admin Sphere API Settings for project-payment-21                            |
    | CT_CLIENT_SECRET          | see admin Sphere API Settings for project-payment-21                            |
    | CT_PAYONE_INTEGRATION_URL | https://ct-payone-integration-test.herokuapp.com/commercetools/handle/payments/ |
    
  - *CT_PAYONE_INTEGRATION_URL* resource with deployed 
    [commercetools payone integration service](https://github.com/commercetools/commercetools-payone-integration).
    This service must be connected to the same project (*CT_PROJECT_KEY*).
    - For current workflow the service is deployed to [Heroku](https://ct-payone-integration-test.herokuapp.com/)
    using *heroku cli*. To avoid any side-effect of previous tests it is recommended to re-start the service 
    before the build (this will re-initialize all custom types, see [IntegrationService#start()](https://github.com/commercetools/commercetools-payone-integration/blob/927adfa637918c20feb03242242f9d57f5561669/service/src/main/java/com/commercetools/pspadapter/payone/IntegrationService.java#L52)):
      ```
      heroku restart --app ct-payone-integration-test
      ```
      This step requires locally [installed and logged in heroku CLI](https://devcenter.heroku.com/articles/heroku-command-line).

If you have all above - run the tests (both unit and integration):
```
heroku restart --app ct-payone-integration-test # may be skipped on secondary run
sbt clean test it:test
```

# Publish workflow

### Publish to local maven repo
 
This step may be used local tests of SNAPSHOT version:
```
sbt clean test it:test publishM2
```

## Publish to Maven Central

### **Note**: set proper version in `version.sbt` before publishing to central repo.

If you are a new developer in the project - update contributors list in `build.sbt -> commonSettings -> pomExtra`.

Publishing to Maven Central requires the next steps:

 1. Build the app (see the steps above for integration tests)
 2. [Signing up the app with PGP key](#signing-up-the-app-with-pgp-key)
 3. [Deploy to OSS Sonatype](#3-deploy-to-OSS-Sonatype)
 4. [Manually release from Sonatype web page to Maven Central](#manually-release-from-sonatype-web-page-to-maven-central)

**Note**: to publish only to Sonatype PGP signing is not required.
 
### 2. Signing up the app with PGP key

This step requires installed [PGP tool](https://gpgtools.org/), 
[published PGP key](http://security.stackexchange.com/questions/406/how-should-i-distribute-my-public-key) and 
[configured SBT PGP plugin](http://www.scala-sbt.org/sbt-pgp/). 

More info about SBT publishing find here:

http://www.scala-sbt.org/0.13/docs/Using-Sonatype.html

http://www.scala-sbt.org/0.12.4/docs/Detailed-Topics/Publishing.html

### 3. Deploy to OSS Sonatype

Current `sbt` build script expects to find Sonatype login/password in environment variables `$NEXUS_USER`/ `$NEXUS_PASS`. 

For simple publishing to Sonatype `sbt publish` may be used, but in this case it will not be able to deploy to 
Maven Central. To publish signed app use `sbt publish-signed`:
```
heroku restart --app ct-payone-integration-test # may be skipped on secondary run
sbt clean test it:test publish-signed
```

This process will ask passphrase for you PGP private key before signing the application.

### 4. Manually release from Sonatype web page to Maven Central


