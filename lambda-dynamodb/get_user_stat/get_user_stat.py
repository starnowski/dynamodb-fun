import os
import json


# function must be called 'handler'
def handler(event, context):
    if event:

        try:
            event['name']
            name = event['name']
            value = os.environ.get(name)
            output_string = 'Value for {} is {}'.format(name, value)

        except KeyError:
            output_string = 'A name was not defined in the event payload'

    return output_string