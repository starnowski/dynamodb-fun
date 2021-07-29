import boto3
import os

host = os.environ.get('DYNAMODB_HOST')
port = os.environ.get('DYNAMODB_PORT')
print("database host %s " % host)
print("database port %s " % port)
dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)


def create_lead_table(dynamodb):
    table = dynamodb.create_table(
        TableName='leads',
        KeySchema=[
            {
                'AttributeName': 'name',
                'KeyType': 'HASH'
            },
            {
                'AttributeName': 'type',
                'KeyType': 'RANGE'
            }
        ],
        AttributeDefinitions=[
            {
                'AttributeName': 'name',
                'AttributeType': 'S'
            },
            {
                'AttributeName': 'type',
                'AttributeType': 'S'
            }
        ],
        ProvisionedThroughput={
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )

    print("Created table 'leads' %s " % table)
    # Wait until the table exists.
    table.meta.client.get_waiter('table_exists').wait(TableName='leads')


def create_user_stats_table(dynamodb):
    table = dynamodb.create_table(
        TableName='user_stats',
        KeySchema=[
            {
                'AttributeName': 'user_id',
                'KeyType': 'HASH'
            },
            {
                'AttributeName': 'timestamp',
                'KeyType': 'RANGE'
            }
        ],
        AttributeDefinitions=[
            {
                'AttributeName': 'user_id',
                'AttributeType': 'S'
            },
            {
                'AttributeName': 'timestamp',
                'AttributeType': 'N'
            }
        ],
        ProvisionedThroughput={
            'ReadCapacityUnits': 5,
            'WriteCapacityUnits': 5
        }
    )

    print("Created table 'user_stats' %s " % table)
    # Wait until the table exists.
    table.meta.client.get_waiter('table_exists').wait(TableName='user_stats')


create_lead_table(dynamodb)
create_user_stats_table(dynamodb)
