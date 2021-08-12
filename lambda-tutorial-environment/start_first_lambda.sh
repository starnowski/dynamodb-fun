#!/usr/bin/env bash

#DYNAMO_DB_HOST
docker run -p 9100:8080 -e DYNAMO_DB_HOST='http4://localxxxhost.com' lambda_print_env