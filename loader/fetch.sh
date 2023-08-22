#!/bin/bash

shopt -s globstar

VERSION='3.0.0'

curl -L https://github.com/spring-projects/spring-boot/archive/v${VERSION}.tar.gz -o spring-boot.tar.gz

tar -x -f spring-boot.tar.gz \
       -C src/main/java/extrarulesjava/loader/ \
       spring-boot-${VERSION}/spring-boot-project/spring-boot-tools/spring-boot-loader/src/main/java/org/springframework/boot/loader/ \
       --strip-components=11

rm -rf src/**/{jarmode,util}/ \
       src/**/{Jar,Properties,War}Launcher.java \
       src/**/package-info.java

sed -i 's/org.springframework.boot.loader/extrarulesjava.loader/g' src/**/*.java
