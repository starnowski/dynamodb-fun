#!/usr/bin/env bash

## Remove database files
rm -rf ./docker/dynamodb/*

export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost

python3 -m pip install -r requirements.txt

## Run specific tests
./run_python_tests.sh


