#!/usr/bin/env bash

VERSION="$1"

RED='\033[0;31m'
YELLOW='\033[0;49;33m'
GREEN='\033[0;32m'
LIGHT_BLUE='\033[1;34m'
NC='\033[0m'

if [ -z "$VERSION" ] ; then
#  printf "${RED}First argument as a build version is required${NC}\n"
#  exit 1
  VERSION=development-SNAPSHOT
  printf "\n${YELLOW}[Warning] Version is not specified, default version \"${VERSION}\" will be used: ${NC}\n\n"
else
  printf "\n${GREEN}Build and deploy a new application version ${LIGHT_BLUE}${VERSION}${NC}\n\n"
fi

# Build, test and Publish Docs to Github pages and artifacts to Bintray
# See BUILD.md for more details about required environment to test and deploy the application
heroku restart --app ct-payment-integration-java
#./gradlew --info clean build install -Dbuild.version="${VERSION}"
./gradlew --info clean build publishToMavenLocal -Dbuild.version="${VERSION}"

if [ $? -eq 0 ]; then
    printf "\n${GREEN}\"${VERSION}\" is published to maven local repository ~/.m2/repository${NC}\n\n"
else
    printf "\n${RED}The build is failed${NC}\n\n"
fi