import unittest
import boto3
import os


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        host = os.environ['DYNAMODB_HOST']
        port = os.environ['DYNAMODB_PORT']
        print("database host %s " % host)
        print("database port %s " % port)
        self.dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)

    def test_default_ec2_user(self):
        # Create the DynamoDB table.
        table = self.dynamodb.create_table(
            TableName='users',
            KeySchema=[
                {
                    'AttributeName': 'username',
                    'KeyType': 'HASH'
                },
                {
                    'AttributeName': 'last_name',
                    'KeyType': 'RANGE'
                }
            ],
            AttributeDefinitions=[
                {
                    'AttributeName': 'username',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'last_name',
                    'AttributeType': 'S'
                },
            ],
            ProvisionedThroughput={
                'ReadCapacityUnits': 5,
                'WriteCapacityUnits': 5
            }
        )

        # Wait until the table exists.
        table.meta.client.get_waiter('table_exists').wait(TableName='users')

        # Print out some data about the table.
        print(table.item_count)
        self.assertEqual(table.item_count, 0, "Table should be empty")


if __name__ == '__main__':
    unittest.main()