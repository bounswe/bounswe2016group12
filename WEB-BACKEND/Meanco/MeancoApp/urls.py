from django.conf.urls import url
from .views import *

urlpatterns = [
    url(r'^$', Home.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Search$', SearchList.get_page),
]