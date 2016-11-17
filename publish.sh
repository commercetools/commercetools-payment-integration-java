#!/bin/bash

# Build, test and Publish to Maven Central (actually, target repo is depended on build.sbt settings)

heroku restart --app ct-payone-integration-test && sbt clean test it:test publish-signed
