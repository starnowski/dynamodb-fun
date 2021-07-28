import os
import boto3
from flask import Flask, jsonify
from flask.json import JSONEncoder
import calendar
from datetime import datetime
from app import routes

app = Flask(__name__)


def setup_app(app):
    # All your initialization code
    host = os.environ.get('DYNAMODB_HOST')
    port = os.environ.get('DYNAMODB_PORT')
    print("flask database host %s " % host)
    print("flask database port %s " % port)
    app.dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)


class CustomJSONEncoder(JSONEncoder):

    def default(self, obj):
        try:
            if isinstance(obj, datetime):
                if obj.utcoffset() is not None:
                    obj = obj - obj.utcoffset()
                millis = int(
                    calendar.timegm(obj.timetuple()) * 1000 +
                    obj.microsecond / 1000
                )
                return millis
            if isinstance(o, decimal.Decimal):
                # wanted a simple yield str(o) in the next line,
                # but that would mean a yield on the line with super(...),
                # which wouldn't work (see my comment below), so...
                return (str(o) for o in [o])
            iterable = iter(obj)
        except TypeError:
            pass
        else:
            return list(iterable)
        return JSONEncoder.default(self, obj)


app.json_encoder = CustomJSONEncoder


setup_app(app)
