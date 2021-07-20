#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

python3() { python3.8 "$@"; }
export -f python3
"${SCRIPT_DIR}/run_local_test.sh"