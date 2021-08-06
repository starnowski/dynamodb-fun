#!/usr/bin/env bash

python3 -m pip install -r requirements.txt
export FLASK_APP=dynamoapp.py
#flask run
python3 dynamoapp.py