import unittest
import boto3
import os


class TestPutRecord(unittest.TestCase):

    def setUp(self):
        host = os.environ['DYNAMODB_HOST']
        port = os.environ['DYNAMODB_PORT']
        print("database host %s " % host)
        print("database port %s " % port)
        self.dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)

    def test_put_item(self):
        # given
        table = self.dynamodb.Table('users')

        # when
        table.put_item(
            Item={
                'username': 'janedoe',
                'first_name': 'Jane',
                'last_name': 'Doe',
                'age': 25,
                'account_type': 'standard_user',
            }
        )

        # then
        response = table.get_item(
            Key={
                'username': 'janedoe',
                'last_name': 'Doe'
            }
        )
        item = response['Item']
        print(item)
        self.assertEqual(item['username'], 'janedoe', "User name should be correct")
        self.assertEqual(item['last_name'], 'Doe', "User surname should be correct")

    def test_update_item(self):
        # given
        table = self.dynamodb.Table('users')
        table.put_item(
            Item={
                'username': 'johndoe',
                'first_name': 'John',
                'last_name': 'Doe',
                'age': 32,
                'account_type': 'standard_user',
            }
        )

        # when
        table.update_item(
            Key={
                'username': 'johndoe',
                'last_name': 'Doe'
            },
            UpdateExpression='SET age = :val1',
            ExpressionAttributeValues={
                ':val1': 27
            }
        )

        # then
        response = table.get_item(
            Key={
                'username': 'johndoe',
                'last_name': 'Doe'
            }
        )
        item = response['Item']
        print(item)
        self.assertEqual(item['username'], 'johndoe', "User name should be correct")
        self.assertEqual(item['last_name'], 'Doe', "User surname should be correct")
        self.assertEqual(item['age'], 27, "User age should be correct")
        self.assertEqual(item['account_type'], 'standard_user', "User account_type should be correct")


if __name__ == '__main__':
    unittest.main()
