#!/usr/bin/env bash

python3.8 -m pip install -r requirements.txt
export FLASK_APP=dynamoapp.py
flask run
