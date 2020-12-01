FROM alpine:latest as packager

RUN mkdir /info
RUN date >/info/build_date.txt

RUN apk --no-cache add openjdk11-jdk openjdk11-jmods

ENV JAVA_MINIMAL="/opt/java-minimal"

# build minimal JRE
RUN /usr/lib/jvm/java-11-openjdk/bin/jlink \
    --verbose \
    --add-modules \
        java.base,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --compress 2 --strip-debug --no-header-files --no-man-pages \
    --release-info="add:IMPLEMENTOR=radistao:IMPLEMENTOR_VERSION=radistao_JRE" \
    --output "$JAVA_MINIMAL"

FROM alpine:latest



ENV JAVA_HOME=/opt/java-minimal
ENV PATH="$PATH:$JAVA_HOME/bin"

COPY --from=packager "$JAVA_HOME" "$JAVA_HOME"

RUN apk --no-cache add nano

RUN apk --no-cache add ffmpeg


ENV PROPERTIES_PATH src/main/resources/application.properties
COPY build/libs/springmvcdemo-1.0.1.jar /var/app.jar
COPY $PROPERTIES_PATH /cfg/application.properties

copy etc/*.* /etc/

EXPOSE 9999

ENTRYPOINT java -jar /var/app.jar --spring.config.location=/cfg/application.properties




