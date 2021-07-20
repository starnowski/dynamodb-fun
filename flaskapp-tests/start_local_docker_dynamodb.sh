#!/usr/bin/env bash

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

python3() { python3.8 "$@"; }
export -f python3


## Remove database files
pushd "${SCRIPT_DIR}"
rm -rf "${SCRIPT_DIR}/docker/dynamodb"
mkdir -p "${SCRIPT_DIR}/docker/dynamodb"
pwd
docker-compose up --detach
popd

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

waitUntilDockerContainerIsReady
#
echo "Installing tests dependencies"
python3 -m pip install -r test-requirements.txt

pushd "${SCRIPT_DIR}/../flaskapp/"
python3 create_tables.py
popd
