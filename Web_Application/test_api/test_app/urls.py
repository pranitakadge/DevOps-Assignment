
# Django related dependencies
from django.urls import re_path,path

# Project related dependencies=
from .views.customer_view import CustomerView

urlpatterns = [
    re_path(r'^customer/(?P<customer_uuid>[0-9a-f-]+)?$',
        CustomerView.as_view()),
]