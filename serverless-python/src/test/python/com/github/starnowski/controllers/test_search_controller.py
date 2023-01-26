import boto3
from moto import mock_dynamodb
from com.github.starnowski.controllers import search_controller as user_stats_search

@mock_dynamodb
def test_search():
    # given
    dynamodb = boto3.resource('dynamodb')
    event = {'body': {"name": "warehouse", "type": "CLOTHES RENTAL", "url": "http://warehouse.com/nosuch_address"}}

    # when
    response = user_stats_search.handle(event, dynamodb)

    # then
    assert response == "is awesome"


