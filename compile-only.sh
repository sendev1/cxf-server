#!/usr/bin/env bash

ARTIFACT=server
MAINCLASS=com.cxf.server.ServerApplication
VERSION=0.0.1-SNAPSHOT

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

mkdir -p build/native-image

JAR="$ARTIFACT-$VERSION.jar"
rm -f $ARTIFACT
echo "Unpacking $JAR"

cd build/native-image
jar -xvf ../libs/$JAR
cp -R META-INF BOOT-INF/classes

LIBPATH=$(find BOOT-INF/lib | tr '\n' ':')
CP=BOOT-INF/classes:$LIBPATH

GRAALVM_VERSION=$(native-image --version)
echo "Compiling $ARTIFACT with $GRAALVM_VERSION"

native-image \
  --verbose \
  --no-fallback \
  --no-server \
  --install-exit-handlers \
  --enable-all-security-services \
  -Dspring.xml.ignore=false \
  -H:GenerateDebugInfo=1 \
  -H:Name=${ARTIFACT} \
  -H:+PrintClassInitialization \
  -Dorg.apache.cxf.JDKBugHacks.all=true \
  -Dspring.native.mode=agent \
  -H:ConfigurationFileDirectories=./src/main/resources/META-INF/native-image \
  --initialize-at-build-time=com.sun.xml.bind.v2.runtime.reflect.opt.Injector,com.sun.xml.bind.v2.schemagen.xmlschema \
  --initialize-at-run-time=org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext \
  -H:+ReportExceptionStackTraces \
  -cp ${CP} \
  ${MAINCLASS}

#   --allow-incomplete-classpath \
#  -Dorg.apache.cxf.jmx.disabled=true \
#  -Dcxf.metrics.enabled=false \
#  -Dmanagement.metrics.binders.jvm.enabled=false \
#  -Dcxf.metrics.enabled=false \
#  --initialize-at-run-time=org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext \
#  -H:TraceClassInitialization=true \
#  -Dspring.native.mode=agent
#  -Dspring.native.remove-xml-support=false \
#  -Dspring.spel.ignore=false \
#  -Dspring.native.remove-yaml-support=false \
#  --initialize-at-run-time=org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext \
