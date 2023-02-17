#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

pwd
ls -lah
rm -rf /app/*
cp -r /app-ro/* /app
cp -r /app-ro/.mvn /app
cd /app

# A selenium tesztek valamiért nem futtan natív módban,
# ezért a teszteket "simán" futtatom, majd natív módban skippelem
/usr/bin/nice -n 20 ./mvnw clean test
/usr/bin/nice -n 20 ./mvnw clean package native:build -Pnative -DskipTests=true

rm -rf /app-target/spring-shell-example
cp -r /app/target/spring-shell-example /app-target
chmod ugo+rwx /app-target/spring-shell-example