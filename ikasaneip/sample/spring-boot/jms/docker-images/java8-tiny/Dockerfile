FROM fabric8/java-centos-openjdk8-jre:1.2.6

LABEL Description="This image contains JDK8 - to be extended by image containing a Java application." Vendor="Ikasan" Version="1.0"
LABEL MAINTAINER "team@ikasan.org"

# Prerequisites.

RUN yum -y update
RUN yum -y install wget tar unzip epel-release
RUN yum -y install jq


ENV JAVA_HOME=/usr/java/default \
    LANG=en_US.UTF-8 \
    LC_ALL=en_US.UTF-8
