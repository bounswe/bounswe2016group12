from django.conf.urls import url
from .views import *

urlpatterns = [
    url(r'^$', Home.get_home),
]