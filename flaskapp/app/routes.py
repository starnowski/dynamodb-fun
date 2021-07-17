from flask import render_template
from app import app
import os


@app.route('/')
@app.route('/index')
def index():
    ec2_id = os.environ.get('DYNAMODB_APP_EC2_ID')
    ec2_type = os.environ.get('DYNAMODB_APP_EC2_TYPE')
    ec2_az = os.environ.get('DYNAMODB_APP_EC2_AZ')
    ec2_ipv4 = os.environ.get('DYNAMODB_APP_EC2_IPV4')

    ec2 = {'id': ec2_id, 'type': ec2_type, 'availability_zone': ec2_az, 'ipv4': ec2_ipv4}
    return render_template('index.html', title='My dynamodb fronted app', ec2=ec2)