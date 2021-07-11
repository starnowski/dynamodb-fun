#!/usr/bin/env bash

export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export LOCAL_DYNAMODB_PORT=9000

aws dynamodb describe-limits --endpoint-url "http://localhost:${LOCAL_DYNAMODB_PORT}" --region us-west-2