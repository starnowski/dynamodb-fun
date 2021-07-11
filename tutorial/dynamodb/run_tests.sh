#!/usr/bin/env bash

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

docker-compose up --detach

export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export LOCAL_DYNAMODB_PORT=900
waitUntilDockerContainerIsReady
## Add on exit handler


