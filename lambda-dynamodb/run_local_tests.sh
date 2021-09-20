#!/usr/bin/env bash

### TODO Copy content form run_local_test.sh
### TODO How get docker container ip

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

## Remove database files
pushd "${SCRIPT_DIR}/.."
rm -rf "${SCRIPT_DIR}/../docker/dynamodb"
mkdir -p "${SCRIPT_DIR}/../docker/dynamodb"
pwd
docker-compose up --detach
popd