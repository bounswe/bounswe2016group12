from django.conf.urls import url
from .views import *
from django.conf.urls import include

urlpatterns = [
    url(r'^$', Home.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Search$', SearchList.get_page),
    url(r'^accounts/', include('django.contrib.auth.urls')),
    url(r'^API/AddTopic', ApiTopic.addTopic),
    url(r'^API/SearchTopic', ApiTopic.searchTopic),
    url(r'^API/AddTopicName', ApiTopic.addTopicName),
    url(r'^API/AddComment', ApiComment.addComment),
    url(r'^API/EditComment', ApiComment.editComment),
    url(r'^API/AddRelation', ApiRelation.addRelation),
    url(r'^API/EditRelation', ApiRelation.editRelation),
    url(r'^API/AddTag', ApiTag.addTag),
]