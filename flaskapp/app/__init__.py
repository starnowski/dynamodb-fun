import os
import boto3
from flask import Flask

app = Flask(__name__)

from app import routes

def setup_app(app):
    # All your initialization code
    host = os.environ.get('DYNAMODB_HOST')
    port = os.environ.get('DYNAMODB_PORT')
    print("flask database host %s " % host)
    print("flask database port %s " % port)
    app.dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)


setup_app(app)
