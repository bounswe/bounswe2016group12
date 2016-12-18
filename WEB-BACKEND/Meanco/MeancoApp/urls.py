from django.conf.urls import url
from .views import *
from django.conf.urls import include
from rest_framework.urlpatterns import format_suffix_patterns
urlpatterns = [
    url(r'^$', Home.get_page, name='home'),
    url(r'^Comment$', Comment.get_page),
    url(r'^Comment$', Comment.get_page),
    url(r'^Search/$', SearchList.get_page),
    url(r'^accounts/', include('django.contrib.auth.urls')),
    url(r'^API/AddTopic', ApiTopic.addTopic, name="addTopic"),
    url(r'^API/SearchTopic', ApiTopic.searchTopic),
    url(r'^API/FollowTopic', ApiTopic.followTopic),
    url(r'^API/GetFollowedTopic', ApiTopic.getFollowedTopics),
    url(r'^API/GetViewedTopic', ApiTopic.getViewedTopics),
    url(r'^API/GetCommentedTopic', ApiTopic.getCommentedTopics),
    url(r'^API/TopicLister', ApiTopic.topicListerGet, name="listTopic"),
    url(r'^API/AddComment', ApiComment.addComment, name="addComment"),
    url(r'^API/GetCommentVoters', ApiComment.getUsersVotes),
    url(r'^API/EditComment', ApiComment.editComment),
    url(r'^API/RateComment', ApiComment.rateComment, name="rate"),
    url(r'^API/AddRelation', ApiRelation.addRelation, name="addRelation"),
    url(r'^API/RateRelation', ApiRelation.rateRelation),
    url(r'^API/AddTag', ApiTag.addTag, name="addTag"),
    url(r'^API/SearchTag', ApiTag.searchTag),
    url(r'^API/Register', ApiAuthentication.register),
    url(r'^API/Login', ApiAuthentication.login),
    url(r'^topic/(?P<id>[0-9]+)', Comment.get_page),
    url(r'^relation/(?P<id>[0-9]+)', Relation.get_page),
    url(r'^API/T/$', ApiTopic.TopicList.as_view()),
    url(r'^API/T/(?P<pk>[0-9]+)/$', ApiTopic.TopicDetail.as_view()),
    url(r'signup$', Home.get_signup_page, name="signup"),
    url(r'^Profile$', Profile.get_page)
]
urlpatterns = format_suffix_patterns(urlpatterns)