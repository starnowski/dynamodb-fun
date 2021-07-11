import unittest
import boto3


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        self.dynamodb = boto3.resource('dynamodb')

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


if __name__ == '__main__':
    unittest.main()