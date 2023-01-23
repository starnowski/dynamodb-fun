#!/bin/bash

tmpfile=${1}
ifconfig
#DYNAMODB_HOST=`/sbin/ip route|awk '/default/ { print $3 }'`
DYNAMODB_HOST=`ifconfig eth0 | grep "inet\b" | cut -d: -f2 | awk '{print $1;}'`
DYNAMODB_HOST=`ifconfig eth0 | grep 'inet' | cut -d' ' -f2`
DYNAMODB_HOST=`ip addr show eth0 | grep "inet\b" | awk '{print $2}' | cut -d/ -f1`
#https://medium.com/@TimvanBaarsen/how-to-connect-to-the-docker-host-from-inside-a-docker-container-112b4c71bc66
DYNAMODB_HOST='host.docker.internal'

cat << SCRIPT > "${tmpfile}"
{
  "DynamoDBLeadsFunction": {
    "DYNAMODB_HOST": "$DYNAMODB_HOST",
    "AWS_DEFAULT_REGION": "us-east-1",
    "AWS_ACCESS_KEY_ID": "DUMMYIDEXAMPLE",
    "AWS_SECRET_ACCESS_KEY": "DUMMYEXAMPLEKEY",
    "STAGE": "DYNAMODB_LOCAL",
    "DYNAMODB_LOCAL_STAGE": "DYNAMODB_LOCAL",
    "DYNAMODB_LOCAL_ACCESS_KEY_ID": "DUMMYIDEXAMPLE",
    "DYNAMODB_LOCAL_SECRET_ACCESS_KEY": "DUMMYEXAMPLEKEY",
    "DYNAMODB_LOCAL_ENDPOINT": "http://${DYNAMODB_HOST}:${DYNAMODB_PORT}",
    "DYNAMODB_LOCAL_REGION": "${AWS_DEFAULT_REGION}"
  }
}
SCRIPT
echo "${tmpfile}"
cat "${tmpfile}"