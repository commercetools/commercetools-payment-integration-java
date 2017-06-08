#!/usr/bin/env bash

# publish SNAPSHOT versions

VERSION="$1"

RED='\033[0;31m'
YELLOW='\033[0;49;33m'
GREEN='\033[0;32m'
LIGHT_BLUE='\033[1;34m'
NC='\033[0m'

if [[ "$VERSION" == *-SNAPSHOT ]] ; then
   printf "\n${GREEN}Build and deploy to OSS a new ${YELLOW}SNAPSHOT${NC} application version ${LIGHT_BLUE}${VERSION}${NC}\n\n"
else
  printf "${RED}Version argument must end with '-SNAPSHOT' substring. Current version argument is ${GREEN}${VERSION}${NC}\n"
  exit 1
fi

heroku restart --app ct-payment-integration-java
./gradlew --info clean build uploadArchives -Dbuild.version="${VERSION}"

if [ $? -eq 0 ]; then
    printf "\n${GREEN}\"${VERSION}\" is published to https://oss.sonatype.org/content/repositories/snapshots/com/commercetools/payment/${NC}\n\n"
else
    printf "\n${RED}The build is failed${NC}\n\n"
fi