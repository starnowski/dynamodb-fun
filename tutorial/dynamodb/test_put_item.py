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


if __name__ == '__main__':
    unittest.main()