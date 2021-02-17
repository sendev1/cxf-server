FROM krisfoster/graal-ee-ol-base:21.0.0.2-JDK8 AS builder

# Here I am using a private Docker image, built by myself, that contains
# the Enterprise Edition.
# I need to use this as support for G1GC is an Enterprise Edition Only feature

RUN mkdir app
COPY . /app
WORKDIR app

# Build the Jar
RUN /app/gradlew build

RUN /app/compile-only.sh