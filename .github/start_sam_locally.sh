#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

function waitUntilSAMLocalIsReady {
    checkCount=1
    timeoutInSeconds=30
    while : ; do
        set +e
        curl http://127.0.0.1:3000
        [[ "$?" -ne 0 && $checkCount -ne $timeoutInSeconds ]] || break
        checkCount=$(( checkCount+1 ))
        echo "Waiting $checkCount seconds for SAM to start"
        sleep 1
    done
    set -e
}

pushd "${SCRIPT_DIR}/../sam_app"
sam local start-api &
popd

waitUntilSAMLocalIsReady