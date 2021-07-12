import unittest
import boto3
import os
from boto3.dynamodb.conditions import Key, Attr


class TestQueryItems(unittest.TestCase):

    @classmethod
    def setUpClass(cls):
        print("setUpClass add data ")
        host = os.environ['DYNAMODB_HOST']
        port = os.environ['DYNAMODB_PORT']
        print("database host %s " % host)
        print("database port %s " % port)
        dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)
        table = dynamodb.Table('users')
        TestQueryItems.init_database_data(table)

    def setUp(self):
        host = os.environ['DYNAMODB_HOST']
        port = os.environ['DYNAMODB_PORT']
        print("database host %s " % host)
        print("database port %s " % port)
        self.dynamodb = boto3.resource('dynamodb', endpoint_url='http://' + host + ':' + port)

    def test_query_items(self):
        # given
        table = self.dynamodb.Table('users')


        # when
        response = table.query(
            KeyConditionExpression=Key('username').eq('johndoe')
        )
        items = response['Items']
        print(items)

        # then
        self.assertEqual(len(items), 1, "Number of records should be correct")

    def test_query_items_by_attributes(self):
        # given
        table = self.dynamodb.Table('users')


        # when
        response = table.scan(
            FilterExpression=Attr('age').lt(27) & Attr('address').exists()
        )
        items = response['Items']
        print(items)

        # then
        self.assertEqual(len(items), 2, "Number of records should be correct")

    def test_query_items_by_nested_attributes(self):
        # given
        table = self.dynamodb.Table('users')


        # when
        response = table.scan(
            FilterExpression=Attr('address.state').eq('CA')
                )
        items = response['Items']
        print(items)


        # then
        self.assertEqual(len(items), 2, "Number of records should be correct")

    def init_database_data(table):
        with table.batch_writer() as batch:
            batch.put_item(
                Item={
                    'account_type': 'standard_user',
                    'username': 'johndoe1',
                    'first_name': 'John',
                    'last_name': 'Doe',
                    'age': 25,
                    'address': {
                        'road': '1 Jefferson Street',
                        'city': 'Los Angeles',
                        'state': 'CA',
                        'zipcode': 90001
                    }
                }
            )
            batch.put_item(
                Item={
                    'account_type': 'super_user',
                    'username': 'janedoering1',
                    'first_name': 'Jane',
                    'last_name': 'Doering',
                    'age': 40,
                    'address': {
                        'road': '2 Washington Avenue',
                        'city': 'Seattle',
                        'state': 'WA',
                        'zipcode': 98109
                    }
                }
            )
            batch.put_item(
                Item={
                    'account_type': 'standard_user',
                    'username': 'bobsmith1',
                    'first_name': 'Bob',
                    'last_name':  'Smith',
                    'age': 18,
                    'address': {
                        'road': '3 Madison Lane',
                        'city': 'Louisville',
                        'state': 'KY',
                        'zipcode': 40213
                    }
                }
            )
            batch.put_item(
                Item={
                    'account_type': 'super_user',
                    'username': 'alicedoe1',
                    'first_name': 'Alice',
                    'last_name': 'Doe',
                    'age': 27,
                    'address': {
                        'road': '1 Jefferson Street',
                        'city': 'Los Angeles',
                        'state': 'CA',
                        'zipcode': 90001
                    }
                }
            )


if __name__ == '__main__':
    unittest.main()
