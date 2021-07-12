#!/usr/bin/env bash

function shutdownDockerContainer {
    lastCommandStatus="$?"
    echo "Shutting down docker container"
    docker-compose down
    exit $lastCommandStatus
}

trap shutdownDockerContainer EXIT SIGINT


## Remove database files
rm -rf ./docker/dynamodb/*
docker-compose up --detach

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

python3 -m pip install -r requirements.txt

## Run specific tests
python3 -m unittest test_create_table.py
python3 -m unittest test_put_item.py


