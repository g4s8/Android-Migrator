# do not execute manually - this scrip for mr Travis C.I.

GIT=git

if [ -z "$TRAVIS_BUILD_NUMBER" ]; then
  echo "TRAVIS_BUILD_NUMBER not found"
  exit 1
fi

if ! [ -f "version.txt" ]; then
  echo "version.txt not found"
  exit 1
fi

VERSION_MAIN=$(cat version.txt)
VERSION_FULL="${VERSION_MAIN}.${TRAVIS_BUILD_NUMBER}"
GIT_TAG="v${VERSION_FULL}"

$GIT config --global user.email "builds@travis-ci.com"
$GIT config --global user.name "Travis CI"
$GIT tag $GIT_TAG -a -m "Deployed new version $TRAVIS_BUILD_NUMBER"
$GIT push origin $GIT_TAG
