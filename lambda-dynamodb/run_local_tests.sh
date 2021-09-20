#!/usr/bin/env bash

### TODO Copy content form run_local_test.sh
### TODO How get docker container ip https://stackoverflow.com/questions/17157721/how-to-get-a-docker-containers-ip-address-from-the-host

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

## Remove database files
pushd "${SCRIPT_DIR}/.."
rm -rf "${SCRIPT_DIR}/../docker/dynamodb"
mkdir -p "${SCRIPT_DIR}/../docker/dynamodb"
pwd
docker-compose up --detach
popd