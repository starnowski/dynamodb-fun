#!/usr/bin/env bash

DIRNAME="$(dirname $0)"

python3() { python3.8 "$@"; }
export -f python3
"${DIRNAME}/run_local_test.sh"