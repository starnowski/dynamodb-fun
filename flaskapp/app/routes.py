from flask import render_template, Flask, jsonify, request
from app import app
import os
from boto3.dynamodb.conditions import Key


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

    table = app.dynamodb.Table('leads')
    resp = table.put_item(
        Item={
            'name': name,
            'type': type,
            'url': url
        }
    )
    print(resp)
    return jsonify({
        'name': name,
        'type': type,
        'url': url
    })


@app.route("/user_stats", methods=["POST"])
def create_user_stats():
    data = request.get_json() or {}
    print(data)
    user_id = data.get('user_id')
    timestamp = data.get('timestamp')
    if not user_id or not timestamp:
        return jsonify({'error': 'Please provide user_id and timestamp'}), 400

    weight = data.get('weight')
    blood_pressure = data.get('blood_pressure')

    table = app.dynamodb.Table('user_stats')
    resp = table.put_item(
        Item={
            'user_id': user_id,
            'timestamp': timestamp,
            'weight': weight,
            'blood_pressure': blood_pressure
        }
    )
    print(resp)
    return jsonify({
        'user_id': user_id,
        'timestamp': timestamp,
        'weight': weight,
        'blood_pressure': blood_pressure
    })


@app.route("/user_stats/search", methods=["POST"])
def search_user_stats():
    data = request.get_json() or {}
    print(data)
    user_id = data.get('user_id')
    if not user_id:
        return jsonify({'error': 'Please provide user_id'}), 400

    table = app.dynamodb.Table('user_stats')
    resp = table.query(
        KeyConditionExpression=Key('user_id').eq(user_id)
    )
    print('Search response %s ' % resp['Items'])
    return jsonify({
        'results': resp['Items']
    })


if __name__ == '__main__':
    app.run()
