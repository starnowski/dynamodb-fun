import unittest
import os
import requests
from datetime import datetime, timedelta
import boto3


class TestGetUserStatsItem(unittest.TestCase):

    def setUp(self):
        self.port = os.environ['LAMBDA_PORT']
        self.host = "localhost:" + self.port
        print("FLASK_APP_HOST host %s " % self.host)
        self.test_start = datetime.utcnow().isoformat()
        self.user_id = "GET_STAT_LAMBDA_TEST1"

    def steps_1_create_user_stat(self):
        # given
        table = self.dynamodb.Table('users')

        # when
        resp = table.put_item(
            Item={
                'user_id': user_id,
                'timestamp': Decimal(datetime.timestamp(self.test_start)),
                'weight': 82,
                'blood_pressure': 122
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

    def steps_2_get_user_stat_vi_lambda(self):
        # given
        payload = {'user_id': '1'}

        # when
        response = requests.post(self.host + '/user_stats/search', json=payload)

        # then
        print('Response for user_stats search object %s ' % response)
        json = response.json()
        print('Response json %s ' % json)
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('results' in json, "The results should be part of response")
        stat = json['results'][0]
        self.assertTrue('timestamp' in stat, "The timestamp should be part of response")
        self.assertTrue('weight' in stat, "The weight should be part of response")
        self.assertTrue('blood_pressure' in stat, "The blood_pressure should be part of response")
        self.assertEqual(stat['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(stat['weight'], 83, "The item weight should be correct")
        self.assertEqual(stat['blood_pressure'], 123, "The item blood_pressure should be correct")

    def _steps(self):
        for name in dir(self): # dir() result is implicitly sorted
            if name.startswith("step"):
                yield name, getattr(self, name)

    def test_steps(self):
        for name, step in self._steps():
            try:
                step()
            except Exception as e:
                self.fail("{} failed ({}: {})".format(step, type(e), e))


if __name__ == '__main__':
    unittest.main()