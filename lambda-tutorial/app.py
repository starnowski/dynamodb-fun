import json


# function must be called 'handler'
def handler(event, context):
    if event:

        try:
            event['name']
            name = event['name']
            output_string = 'Welcome {} by lambda'.format(name.capitalize())

        except KeyError:
            output_string = 'A name was not defined in the event payload'

    return output_string