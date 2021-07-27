import unittest
import os
import requests
from datetime import datetime


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        self.host = os.environ['FLASK_APP_HOST']
        print("FLASK_APP_HOST host %s " % self.host)

    def test_create_lead(self):
        # given
        payload = {'user_id': '1', 'timestamp': datetime.utcnow().isoformat(), 'weight': 83, 'blood_pressure': 123}

        # when
        response = requests.post(self.host + '/leads', json=payload)

        # then
        print('Response for lead object %s ' % response)
        json = response.json()
        print('Response json %s ' % json)
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('user_id' in json, "The user_id should be part of response")
        self.assertTrue('timestamp' in json, "The timestamp should be part of response")
        self.assertTrue('weight' in json, "The weight should be part of response")
        self.assertTrue('blood_pressure' in json, "The blood_pressure should be part of response")
        self.assertEqual(json['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(json['weight'], '83', "The item weight should be correct")
        self.assertEqual(json['blood_pressure'], '123', "The item blood_pressure should be correct")


if __name__ == '__main__':
    unittest.main()