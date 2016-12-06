from django.conf.urls import url
from .views import *
from django.conf.urls import include
from rest_framework.urlpatterns import format_suffix_patterns
urlpatterns = [
    url(r'^$', Home.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Search$', SearchList.get_page),
    url(r'^accounts/', include('django.contrib.auth.urls')),
    url(r'^API/AddTopic', ApiTopic.addTopic),
    url(r'^API/SearchTopic', ApiTopic.searchTopic),
    url(r'^API/AddComment', ApiComment.addComment),
    url(r'^API/EditComment', ApiComment.editComment),
    url(r'^API/RateComment', ApiComment.rateComment),
    url(r'^API/AddRelation', ApiRelation.addRelation),
    url(r'^API/RateRelation', ApiRelation.rateRelation),
    url(r'^API/AddTag', ApiTag.addTag),
    url(r'^API/SearchTag', ApiTag.searchTag),
    url(r'^topic/(?P<id>[0-9]+)', Comment.get_page),
    url(r'^API/T/$', ApiTopic.TopicList.as_view()),
    url(r'^API/T/(?P<pk>[0-9]+)/$', ApiTopic.TopicDetail.as_view()),
]
urlpatterns = format_suffix_patterns(urlpatterns)