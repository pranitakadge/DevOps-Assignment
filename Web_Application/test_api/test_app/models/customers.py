import uuid
from mongoengine import *
import datetime

class Customers(Document):
    uuid = UUIDField(binary=False, required=True)
    name = StringField()
    address = StringField()
    created_dttm = DateTimeField(default=datetime.datetime.utcnow)
    updated_dttm = DateTimeField(default=datetime.datetime.utcnow)

    meta = {
        "strict": False
    }
