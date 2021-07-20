from flask import render_template, Flask, jsonify, request
from app import app
import os
import boto3


host = os.environ.get('DYNAMODB_HOST')
port = os.environ.get('DYNAMODB_PORT')
print("database host %s " % host)
print("database port %s " % port)
dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)



@app.route('/')
@app.route('/index')
def index():
    ec2_id = os.environ.get('DYNAMODB_APP_EC2_ID')
    ec2_type = os.environ.get('DYNAMODB_APP_EC2_TYPE')
    ec2_az = os.environ.get('DYNAMODB_APP_EC2_AZ')
    ec2_ipv4 = os.environ.get('DYNAMODB_APP_EC2_IPV4')

    ec2 = {'id': ec2_id, 'type': ec2_type, 'availability_zone': ec2_az, 'ipv4': ec2_ipv4}
    return render_template('index.html', title='My dynamodb fronted app', ec2=ec2)


@app.route("/leads", methods=["POST"])
def create_user():
    data = request.get_json() or {}
    print(data)
    name = data.get('name')
    type = data.get('type')
    url = data.get('url')
    if not name or not type:
        return jsonify({'error': 'Please provide name and type'}), 400

    table = dynamodb.Table('users')
    resp = table.put_item(
        Item={
            'name': {'S': name },
            'type': {'S': type },
            'url': {'S': url }
        }
    )
    print(resp)
    return jsonify({
        'name': name,
        'type': type,
        'url': url
    })