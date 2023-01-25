import simplejson as json
import boto3
from boto3.dynamodb.conditions import Key
from datetime import datetime
from decimal import Decimal


def handle(event, dynamodb):
    data = json.loads(event['body']) or {}
    print(data)
    user_id = data.get('user_id')
    if not user_id:
        return {"statusCode": 400,
                'error': 'Please provide user_id'}

    table = dynamodb.Table('user_stats')
    primary_key = Key('user_id').eq(user_id)
    kwargs = {'KeyConditionExpression': primary_key}

    limit = data.get('limit')
    if limit:
        kwargs['Limit'] = limit

    after_timestamp = data.get('after_timestamp')
    if after_timestamp:
        dt_object = datetime.fromisoformat(after_timestamp)
        primary_key = primary_key & Key('timestamp').gte(Decimal(datetime.timestamp(dt_object)))
        kwargs['KeyConditionExpression'] = primary_key

    resp = table.query(**kwargs)
    print('Search response %s ' % resp['Items'])
    return {
        "statusCode": 200,
        "body": json.dumps({
            'results': covert_dynamodb_list(resp['Items'])
        })
    }


def enrich_with_correct_timestap(item):
    dt_object = datetime.fromtimestamp(item['timestamp'])
    item['timestamp'] = dt_object.utcnow().isoformat()
    return item


def covert_dynamodb_list(items):
    return list(map(enrich_with_correct_timestap, items))
