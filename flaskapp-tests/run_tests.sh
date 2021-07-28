#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"


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

function shutdownApp {
    lastCommandStatus="$?"
    set +e

    echo "Shutting down flask app with pid $FLASK_APP_PID"
    kill -9 $FLASK_APP_PID
    kill -9 $(lsof -t -i:${DYNAMODB_PORT})

    exit $lastCommandStatus
}

trap shutdownApp EXIT SIGINT

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

#
echo "Installing tests dependencies"
python3 -m pip install -r test-requirements.txt

pushd "${SCRIPT_DIR}/../flaskapp/"
python3 create_tables.py
popd

pushd "${SCRIPT_DIR}/../flaskapp/"
./run_app.sh &
FLASK_APP_PID=$!
popd

echo "Flask app pid is $FLASK_APP_PID"

export FLASK_APP_PORT=5000
## Run specific tests
export FLASK_APP_HOST="http://localhost:5000"
waitUntilHttpServerIsReady

"${SCRIPT_DIR}/run_python_tests.sh"