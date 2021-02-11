.PHONY: build

build:
	./gradlew build

compile: build
	./compile-only.sh

profile:
	echo "profiling to generate config.."
	java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar build/libs/server-0.0.1-SNAPSHOT.jar -Dorg.apache.cxf.JDKBugHacks.all=true

clean:
	./gradlew clean

run:
	./build/native-image/server -Dorg.apache.cxf.JDKBugHacks.all=true