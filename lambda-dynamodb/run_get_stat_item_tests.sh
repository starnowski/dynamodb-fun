#!/usr/bin/env bash

### TODO How get docker container ip https://stackoverflow.com/questions/17157721/how-to-get-a-docker-containers-ip-address-from-the-host

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

function buildDockerImage {
    pushd "${SCRIPT_DIR}/get_user_stat"
      ./build_image.sh
    popd
}

function runDockerImage() {
    docker run --detach -p "${LAMBDA_PORT}:8080" get_user_stats
}

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
    set +e

    echo "Shutting down docker container"
    docker rm $(docker stop $(docker ps -a -q --filter ancestor=get_user_stats --format="{{.ID}}"))

    exit $lastCommandStatus
}

export LAMBDA_PORT=9100

waitUntilDockerContainerIsReady

"${SCRIPT_DIR}/run_tests.sh"