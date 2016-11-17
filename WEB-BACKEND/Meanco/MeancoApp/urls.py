from django.conf.urls import url
from .views import *
from django.conf.urls import include

urlpatterns = [
    url(r'^$', Home.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Search$', SearchList.get_page),
    url(r'^accounts/', include('django.contrib.auth.urls')),
    url(r'^topic/(?P<id>[0-9]+)', Comment.get_page),
]