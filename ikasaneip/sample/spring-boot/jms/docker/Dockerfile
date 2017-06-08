FROM registry.access.redhat.com/redhat-openjdk-18/openjdk18-openshift
LABEL Description="This image is used to launch the sample Spring Boot application." Vendor="Ikasan" Version="1.0"
LABEL MAINTAINER "team@ikasan.org"

ENV TERM xterm
ENV DIRPATH /deployments
ENV CONF_PATH /deployments/conf
ENV JAVA_JVM_ARGS -Xms512M -Xmx1024M

RUN mkdir -p $DIRPATH && \
    mkdir -p $CONF_PATH

COPY ${project.exec.artifactId}.jar $DIRPATH/${project.exec.artifactId}.jar

EXPOSE 8080