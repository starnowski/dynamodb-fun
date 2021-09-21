#!/usr/bin/env bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

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

"${SCRIPT_DIR}/run_python_tests.sh"