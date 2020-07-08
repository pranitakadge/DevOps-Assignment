import json
import base64

from rest_framework import status
from rest_framework.views import APIView
from rest_framework.response import Response

from ..services.customer_service import GetCustomerService


class CustomerView(APIView):

    def post(self, request, customer_uuid, format=None):
        try:
            # call service
            data = {}
            result = GetCustomerService().insert_customer_details(customer_uuid, request.data)
            data["result"] = result
            data["status_code"] = 200

        except Exception as error:
            print(error)

        finally:
            return Response(data, status=status.HTTP_200_OK, headers=None)
