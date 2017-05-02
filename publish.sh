#!/bin/bash

VERSION="$1"

RED='\033[0;31m'
GREEN='\033[0;32m'
LIGHT_BLUE='\033[1;34m'
NC='\033[0m'

if [ -z "$VERSION" ] ; then
  printf "${RED}First argument as a build version is required${NC}\n"
  exit 1
else
  printf "\n${GREEN}Build and deploy a new application version ${LIGHT_BLUE}${VERSION}${NC}\n\n"
fi

# Build, test and Publish Docs to Github pages and artifacts to Bintray
# See BUILD.md for more details about required environment to test and deploy the application
heroku restart --app ct-payment-integration-java && ./gradlew clean build aggregateJavaDoc gitPublishPush bintrayUpload -Dbuild.version="$1"
