#!/bin/bash

# Build, test and Publish to Maven Central (actually, target repo is depended on build.sbt settings)
# See BUILD.md for more details about required environment to test and deploy the application
#heroku restart --app ct-payone-integration-test && sbt clean test it:test publish-signed

# Build, test and Publish to Bintray
# See BUILD.md for more details about required environment to test and deploy the application
heroku restart --app ct-payone-integration-test && ./gradlew --info clean build bintrayUpload
