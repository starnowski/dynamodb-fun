#!/usr/bin/env bash

DIRNAME="$(dirname $0)"

function waitUntilDockerContainerIsReady {
    checkCount=1
    timeoutInSeconds=30
    while : ; do
        set +e
        ./test_docker_image.sh
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for database to start"
        sleep 1
    done
    set -e
}

function shutdownDockerContainer {
    lastCommandStatus="$?"
    echo "Shutting down docker container"
    docker-compose down
    exit $lastCommandStatus
}

trap shutdownDockerContainer EXIT SIGINT


## Remove database files
rm -rf "${DIRNAME}/docker/dynamodb/*"
docker-compose up --detach

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

waitUntilDockerContainerIsReady
python3 -m venv ./docker/dynamodb/newvirtualenv
python3 -m pip install -r requirements.txt


## Run specific tests
./run_python_tests.sh

