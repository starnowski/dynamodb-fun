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
tmpfile=$(mktemp)
ifconfig
#DYNAMODB_HOST=`/sbin/ip route|awk '/default/ { print $3 }'`
DYNAMODB_HOST=`ifconfig eth0 | grep "inet\b" | cut -d: -f2 | awk '{print $1;}'`
#DYNAMODB_HOST=`ip addr show eth0 | grep "inet\b" | awk '{print $2}' | cut -d/ -f1`
cat << SCRIPT > "${tmpfile}"
{
  "DynamoDBFunction": {
    "DYNAMODB_HOST": "$DYNAMODB_HOST"
  }
}
SCRIPT
echo "${tmpfile}"
cat "${tmpfile}"

pushd "${SCRIPT_DIR}/../sam_app"
sam local start-api --env-vars "${tmpfile}" &
popd

waitUntilSAMLocalIsReady