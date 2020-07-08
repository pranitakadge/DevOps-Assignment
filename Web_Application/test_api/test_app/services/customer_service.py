# python dependencies
import uuid
import json
import logging

from django.core.exceptions import ValidationError

from test_app.models import Customers

class GetCustomerService:

    def insert_customer_details(self, customer_id,data):
        try:
            result = {}
            
            customer = Customers(
                name=data["name"],
                uuid=customer_id,
                address=data["address"]
            )

            customer.save()
            result="customer added"
            return result

        except Exception as error:
            print("general error -- ",error)

    