import json
import boto3
import os
# import requests


def lambda_handler(event, context):
    """Sample pure Lambda function

    Parameters
    ----------
    event: dict, required
        API Gateway Lambda Proxy Input Format

        Event doc: https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html#api-gateway-simple-proxy-for-lambda-input-format

    context: object, required
        Lambda Context runtime methods and attributes

        Context doc: https://docs.aws.amazon.com/lambda/latest/dg/python-context-object.html

    Returns
    ------
    API Gateway Lambda Proxy Output Format: dict

        Return doc: https://docs.aws.amazon.com/apigateway/latest/developerguide/set-up-lambda-proxy-integrations.html
    """

    # try:
    #     ip = requests.get("http://checkip.amazonaws.com/")
    # except requests.RequestException as e:
    #     # Send some context about this error to Lambda Logs
    #     print(e)

    #     raise e

    if os.getenv("DYNAMODB_HOST"):
        dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + os.getenv("DYNAMODB_HOST") + ':9000')
    else:
        dynamodb = boto3.resource('dynamodb')
    if event['resource'] == "/leads" and event['httpMethod'] == "POST":
        data = json.loads(event['body'])  or {}
        print(data)
        name = data.get('name')
        type = data.get('type')
        url = data.get('url')
        if not name or not type:
            return {"statusCode": 400,
                    'error': 'Please provide name and type'}

        table = dynamodb.Table('leads')
        resp = table.put_item(
            Item={
                'name': name,
                'type': type,
                'url': url
            }
        )
        print(resp)
        return {
            "statusCode": 200,
            "body": json.dumps({
            'name': name,
            'type': type,
            'url': url
            })
        }

#
    if event['resource'] == "/user_stats" and event['httpMethod'] == "POST":
        data = json.loads(event['body'])  or {}
        print(data)
        user_id = data.get('user_id')
        timestamp = data.get('timestamp')
        if not user_id or not timestamp:
            return {"statusCode": 400,
                    'error': 'Please provide user_id and timestamp'}

        weight = data.get('weight')
        blood_pressure = data.get('blood_pressure')

        table = dynamodb.Table('user_stats')
        timestamp_val = datetime.fromisoformat(timestamp)
        resp = table.put_item(
            Item={
                'user_id': user_id,
                'timestamp': Decimal(datetime.timestamp(timestamp_val)),
                'weight': weight,
                'blood_pressure': blood_pressure
            }
        )
        print(resp)
        return {
            "statusCode": 200,
            "body": json.dumps({
                'user_id': user_id,
                'timestamp': timestamp,
                'weight': weight,
                'blood_pressure': blood_pressure
            })
        }
#
    return {
        "statusCode": 200,
        "body": json.dumps({
            "message": "hello world",
            # "location": ip.text.replace("\n", "")
        }),
    }
