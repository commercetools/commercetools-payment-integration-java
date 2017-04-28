#! /bin/bash

set -e

echo "TRAVIS_PULL_REQUEST $TRAVIS_PULL_REQUEST"
echo "TRAVIS_TAG $TRAVIS_TAG"

export TAG=`if [ "$TRAVIS_PULL_REQUEST" = "false" -a -n "$TRAVIS_TAG" ] ; then echo "$TRAVIS_TAG" ; fi`

if [ "$TAG" ]; then
  echo "Upload tag $TAG to bintray"
  ./gradlew --no-daemon --info bintrayUpload
else
  echo "The tag is empty - publish is skipped"
fi
