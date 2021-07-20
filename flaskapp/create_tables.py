import boto3
import os

host = os.environ.get('DYNAMODB_HOST')
port = os.environ.get('DYNAMODB_PORT')
print("database host %s " % host)
print("database port %s " % port)
dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)

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

# Wait until the table exists.
table.meta.client.get_waiter('table_exists').wait(TableName='leads')
print("Created table 'leads'")
