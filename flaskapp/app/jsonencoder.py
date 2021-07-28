from flask.json import JSONEncoder
import calendar
from datetime import datetime
import decimal


class CustomJSONEncoder(JSONEncoder):

    def default(self, obj):
        try:
            if isinstance(obj, datetime):
                if obj.utcoffset() is not None:
                    obj = obj - obj.utcoffset()
                millis = int(
                    calendar.timegm(obj.timetuple()) * 1000 +
                    obj.microsecond / 1000
                )
                return millis
            if isinstance(obj, decimal.Decimal):
                # wanted a simple yield str(obj) in the next line,
                # but that would mean a yield on the line with super(...),
                # which wouldn't work (see my comment below), so...
                f = float(obj)
                if f.is_integer():
                    return int(f)
                return f
            iterable = iter(obj)
        except TypeError:
            pass
        else:
            return list(iterable)
        return JSONEncoder.default(self, obj)
