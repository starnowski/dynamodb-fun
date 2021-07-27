import unittest
import os
import requests
from datetime import datetime


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        self.host = os.environ['FLASK_APP_HOST']
        print("FLASK_APP_HOST host %s " % self.host)

    def steps_create_user_stat(self):
        # given
        payload = {'user_id': '1', 'timestamp': datetime.utcnow().isoformat(), 'weight': 83, 'blood_pressure': 123}

        # when
        response = requests.post(self.host + '/user_stats', json=payload)

        # then
        print('Response for user_stats object %s ' % response)
        json = response.json()
        print('Response json %s ' % json)
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('user_id' in json, "The user_id should be part of response")
        self.assertTrue('timestamp' in json, "The timestamp should be part of response")
        self.assertTrue('weight' in json, "The weight should be part of response")
        self.assertTrue('blood_pressure' in json, "The blood_pressure should be part of response")
        self.assertEqual(json['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(json['weight'], 83, "The item weight should be correct")
        self.assertEqual(json['blood_pressure'], 123, "The item blood_pressure should be correct")

    def steps_get_user_stat(self):
        # given
        payload = {'user_id': '1'}

        # when
        response = requests.post(self.host + '/user_stats/search', json=payload)

        # then
        print('Response for user_stats object %s ' % response)
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