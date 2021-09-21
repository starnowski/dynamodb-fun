#!/usr/bin/env bash

### TODO Copy content form run_local_test.sh
### TODO How get docker container ip https://stackoverflow.com/questions/17157721/how-to-get-a-docker-containers-ip-address-from-the-host

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

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

function waitUntilHttpServerIsReady {
    checkCount=1
    timeoutInSeconds=30
    while : ; do
        set +e
        curl "${FLASK_APP_HOST}"
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for flask app to start"
        sleep 1
    done
    set -e
}

function shutdownDockerContainer {
    lastCommandStatus="$?"
    set +e

    echo "Shutting down flask app with pid $FLASK_APP_PID"
    kill $FLASK_APP_PID

    echo "Shutting down docker container"
    pushd "${SCRIPT_DIR}"
    docker-compose down
    popd

    echo "Removing local database"
    ls -la "${SCRIPT_DIR}/../docker/dynamodb/"
    rm -rf "${SCRIPT_DIR}/../docker/dynamodb/"

    exit $lastCommandStatus
}


## Remove database files
pushd "${SCRIPT_DIR}/.."
rm -rf "${SCRIPT_DIR}/../docker/dynamodb"
mkdir -p "${SCRIPT_DIR}/../docker/dynamodb"
pwd
docker-compose up --detach
popd

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

waitUntilDockerContainerIsReady

"${SCRIPT_DIR}/run_tests.sh"