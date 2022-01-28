#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

function waitUntilSLSLocalIsReady {
    checkCount=1
    timeoutInSeconds=30
    while : ; do
        set +e
        curl http://127.0.0.1:3000/dev
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for SAM to start"
        sleep 1
    done
    set -e
}

export STAGE="DYNAMODB_LOCAL"
export DYNAMODB_LOCAL_STAGE="DYNAMODB_LOCAL"
export DYNAMODB_LOCAL_ACCESS_KEY_ID="DUMMYIDEXAMPLE"
export DYNAMODB_LOCAL_SECRET_ACCESS_KEY="DUMMYEXAMPLEKEY"
export DYNAMODB_LOCAL_ENDPOINT="http://127.0.0.1:9000"
export DYNAMODB_LOCAL_REGION="${AWS_DEFAULT_REGION}"

pushd "${SCRIPT_DIR}/../typescript-dynamodb"
npm run start-locally &
popd

waitUntilSLSLocalIsReady