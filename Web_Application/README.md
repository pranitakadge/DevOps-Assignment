# Web-Application  
<br>

This is a RESTful webservice written in [Django REST framework](https://www.django-rest-framework.org/).

# About the Service

This service will make customer onboarding.

# prerequisites

## python dependencies
- Django==2.2
- djangorestframework==3.10.3

## Webservice Endpoints

POST (Saves customer details)

customer/{customer_uuid}

Input:
```sh
{
    "name": "john",
    "address": "malad"
}
```

Output:
```sh
{
    "status_code": 200,
    "result": "customer added"
}
```



