#!/usr/bin/env bash

#TODO Get script path
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
    set +e
    echo "Shutting down flask app with pid $FLASK_APP_PID"
    kill $FLASK_APP_PID
    echo "Shutting down docker container"
    pushd $DIRNAME
    docker-compose down
    popd
    exit $lastCommandStatus
}

trap shutdownDockerContainer EXIT SIGINT


## Remove database files
rm -rf "${DIRNAME}/docker/dynamodb/*"
mkdir -p "${DIRNAME}/docker/dynamodb"
docker-compose up --detach

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

waitUntilDockerContainerIsReady
#
echo "Installing tests dependencies"
python3.8 -m pip install -r test-requirements.txt


pushd ./../flaskapp/
./run_app.sh &
FLASK_APP_PID=$!
popd

echo "Flask app pid is $FLASK_APP_PID"


## Run specific tests
./run_python_tests.sh

