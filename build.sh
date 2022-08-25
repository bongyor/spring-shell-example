#!/bin/bash
set -euox pipefail
IFS=$'\n\t'

docker build -t spring-shell-example-builder .
docker volume create spring-shell-example-builder-m2
docker run \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v "$(pwd)":/app-ro:ro \
  -v "$(pwd)/target":/app-target \
  -v spring-shell-example-builder-m2:/root/.m2 \
  --network=host \
  --name spring-shell-example-build-container \
  --rm \
  --workdir /app \
  spring-shell-example-builder \
  "/app-ro/build-in-docker.sh"