import unittest
import os
import requests


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        self.host = os.environ['FLASK_APP_HOST']
        print("FLASK_APP_HOST host %s " % self.host)

    def test_create_lead(self):
        # given
        payload = {'name': 'warehouse', 'type': 'CLOTHES RENTAL', 'url': 'http://warehouse.com/nosuch_address'}

        # when
        response = requests.post(self.host + '/leads', json=payload)

        # then
        print('Response for lead object %s ' % response)
        print('Response json %s ' % response.json())
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('name' in response.json(), "The name should be part of response")
        self.assertTrue('type' in response.json(), "The type should be part of response")
        self.assertTrue('url' in response.json(), "The type should be part of response")


if __name__ == '__main__':
    unittest.main()