#!/usr/bin/env bash

ARTIFACT=server
MAINCLASS=com.cxf.server.ServerApplication
VERSION=0.0.1-SNAPSHOT

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

rm -rf build
mkdir -p build/native-image

echo "Packaging $ARTIFACT with Maven"
./gradlew build

JAR="$ARTIFACT-$VERSION.jar"
rm -f $ARTIFACT
echo "Unpacking $JAR"

cd build/native-image
jar -xvf ../libs/$JAR
cp -R META-INF BOOT-INF/classes

LIBPATH=`find BOOT-INF/lib | tr '\n' ':'`
CP=BOOT-INF/classes:$LIBPATH

GRAALVM_VERSION=`native-image --version`
echo "Compiling $ARTIFACT with $GRAALVM_VERSION"
native-image \
    --verbose \
    --allow-incomplete-classpath \
    --no-fallback \
    --no-server \
    --enable-all-security-services \
    -H:Name=${ARTIFACT} \
    -H:TraceClassInitialization=true \
    -H:+ReportExceptionStackTraces \
    -Dpring.xml.ignore=false \
    -Dspring.spel.ignore=true \
    -Dspring.native.remove-yaml-support=true \
    -cp ${CP} \
    ${MAINCLASS}

#     --initialize-at-run-time=org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext \