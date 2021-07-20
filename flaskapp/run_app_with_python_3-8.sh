#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

python3() { python3.8 "$@"; }
export -f python3
export DYNAMODB_HOST=localhost
export DYNAMODB_PORT=9000
"${SCRIPT_DIR}/run_app.sh"
