import unittest
import os
import requests
from datetime import datetime, timedelta


class TestCreateTable(unittest.TestCase):

    def setUp(self):
        self.host = os.environ['FLASK_APP_HOST']
        print("FLASK_APP_HOST host %s " % self.host)
        self.test_start = datetime.utcnow()

    def print_json(self, json, text):
        print('Test : %s Response json %s ' % (text, json))

    def print_json_payload(self, json, text):
        print('Test : %s Payload json %s ' % (text, json))

    def steps_1_create_user_stat(self):
        # given
        payload = {'user_id': '1', 'timestamp': self.test_start.isoformat(), 'weight': 83, 'blood_pressure': 123}
        self.print_json_payload(payload, 'steps_1_create_user_stat')


        # when
        response = requests.post(self.host + '/user_stats', json=payload)

        # then
        print('Response for user_stats object %s ' % response)
        print('Response for user_stats content %s ' % response.text)
        json = response.json()
        self.print_json(json, "steps_1_create_user_stat")
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('user_id' in json, "The user_id should be part of response")
        self.assertTrue('timestamp' in json, "The timestamp should be part of response")
        self.assertTrue('weight' in json, "The weight should be part of response")
        self.assertTrue('blood_pressure' in json, "The blood_pressure should be part of response")
        self.assertEqual(json['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(json['weight'], 83, "The item weight should be correct")
        self.assertEqual(json['blood_pressure'], 123, "The item blood_pressure should be correct")

    def steps_2_search_user_stat(self):
        # given
        payload = {'user_id': '1'}

        # when
        response = requests.post(self.host + '/user_stats/search', json=payload)

        # then
        print('Response for user_stats search object %s ' % response)
        json = response.json()
        self.print_json(json, "steps_2_search_user_stat")
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('results' in json, "The results should be part of response")
        stat = json['results'][0]
        self.assertTrue('timestamp' in stat, "The timestamp should be part of response")
        self.assertTrue('weight' in stat, "The weight should be part of response")
        self.assertTrue('blood_pressure' in stat, "The blood_pressure should be part of response")
        self.assertEqual(stat['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(stat['weight'], 83, "The item weight should be correct")
        self.assertEqual(stat['blood_pressure'], 123, "The item blood_pressure should be correct")

    def steps_3_search_get_multiple_user_stat(self):
        # given
        payload1 = {'user_id': '1', 'timestamp': (self.test_start + timedelta(minutes=1)).isoformat(), 'weight': 83, 'blood_pressure': 109}
        payload2 = {'user_id': '1', 'timestamp': (self.test_start + timedelta(minutes=1)).isoformat(), 'weight': 82, 'blood_pressure': 114}
        self.print_json_payload(payload1, 'steps_3_search_get_multiple_user_stat payload1')
        response = requests.post(self.host + '/user_stats', json=payload1)
        self.print_json_payload(payload2, 'steps_3_search_get_multiple_user_stat payload2')
        self.assertEqual(response.status_code, 200, "Second request should be successful")
        response = requests.post(self.host + '/user_stats', json=payload2)
        self.assertEqual(response.status_code, 200, "Third request should be successful")
        search_payload = {'user_id': '1'}

        # when
        response = requests.post(self.host + '/user_stats/search', json=search_payload)

        # then
        print('Response for user_stats search object %s ' % response)
        json = response.json()
        self.print_json(json, "steps_3_search_get_multiple_user_stat")
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('results' in json, "The results should be part of response")
        stat = json['results'][0]
        self.assertEqual(len(json['results']), 3, "The array length should be correct")
        # Sorting should be correct
        self.assertTrue('timestamp' in stat, "The timestamp should be part of response")
        self.assertTrue('weight' in stat, "The weight should be part of response")
        self.assertTrue('blood_pressure' in stat, "The blood_pressure should be part of response")
        self.assertEqual(stat['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(stat['weight'], 83, "The item weight should be correct")
        self.assertEqual(stat['blood_pressure'], 123, "The item blood_pressure should be correct")

    def steps_4_search_get_user_stat_with_limit(self):
        # given
        search_payload = {'user_id': '1', 'limit': 1}

        # when
        response = requests.post(self.host + '/user_stats/search', json=search_payload)

        # then
        print('Response for user_stats search object %s ' % response)
        json = response.json()
        self.print_json(json, "steps_4_search_get_user_stat_with_limit")
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('results' in json, "The results should be part of response")
        self.assertEqual(len(json['results']), 1, "The array length should be correct")

    def steps_5_search_get_user_stat_after_timestamp(self):
        # given
        test_time = self.test_start
        after_timestamp = test_time + timedelta(minutes=5)
        user_stat_timestamp = test_time + timedelta(minutes=15)
        print ("user_stat_timestamp ", user_stat_timestamp, " after_timestamp ", after_timestamp)
        payload = {'user_id': '1', 'timestamp': user_stat_timestamp.isoformat(), 'weight': 81, 'blood_pressure': 156}
        self.print_json_payload(payload, 'steps_5_search_get_user_stat_after_timestamp payload')
        response = requests.post(self.host + '/user_stats', json=payload)
        self.assertEqual(response.status_code, 200, "Request should be successful")
        search_payload = {'user_id': '1', 'limit': 1, 'after_timestamp': after_timestamp.isoformat()}

        # when
        response = requests.post(self.host + '/user_stats/search', json=search_payload)

        # then
        print('Response for user_stats search object %s ' % response)
        json = response.json()
        self.print_json(json, "steps_5_search_get_user_stat_after_timestamp")
        self.assertEqual(response.status_code, 200, "Response should have status 200")
        self.assertTrue('results' in json, "The results should be part of response")
        self.assertEqual(len(json['results']), 1, "The array length should be correct")
        stat = json['results'][0]
        self.assertTrue('timestamp' in stat, "The timestamp should be part of response")
        self.assertTrue('weight' in stat, "The weight should be part of response")
        self.assertTrue('blood_pressure' in stat, "The blood_pressure should be part of response")
        self.assertEqual(stat['user_id'], '1', "The item user_id should be correct")
        self.assertEqual(stat['weight'], 81, "The item weight should be correct")
        self.assertEqual(stat['blood_pressure'], 156, "The item blood_pressure should be correct")

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