FROM ubuntu:18.04
MAINTAINER Zdenek Klar <zdeneki.klar@gmail.com>

RUN mkdir /info
RUN date >/info/build_date.txt

# Update system and install base dependencies
RUN apt-get update && apt-get upgrade -y && apt-get install curl git wget jq -y

RUN apt-get install nano -y

RUN apt-get install ffmpeg -y

RUN apt-get install default-jdk -y

ENV PROPERTIES_PATH src/main/resources/application.properties
COPY build/libs/springmvcdemo-1.0.1.jar /var/app.jar
COPY $PROPERTIES_PATH /cfg/application.properties

copy etc/*.* /etc/

EXPOSE 9999

ENTRYPOINT java -jar /var/app.jar --spring.config.location=/cfg/application.properties
