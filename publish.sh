#!/bin/bash

# Build, test and Publish to Maven Central (actually, target repo is depended on build.sbt settings)
# See BUILD.md for more details about required environment to test and deploy the application
#heroku restart --app ct-payment-integration-java && sbt clean test it:test publish-signed

# Build, test and Publish to Bintray
# See BUILD.md for more details about required environment to test and deploy the application
heroku restart --app ct-payment-integration-java && ./gradlew --info clean build bintrayUpload
