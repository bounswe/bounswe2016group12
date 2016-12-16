from django.shortcuts import render
from sys import platform as _platform

from MeancoApp.models import *
from .forms import UserCreateForm

def get_page(request):
    topics = Topic.objects.all()
    id = request.user.id
    FollowedTopics=()
    CommentedTopics=()
    ViewedTopics=()
    TrendingTopics=Topic.objects.order_by("-view_count")[:5]
    TrendingTags = Tag.objects.order_by("-topic_count")[:5]
    if request.user.is_authenticated:
        profileId = Profile.objects.get(user_id=id).id
        FollowedTopics = FollowedTopic.objects.filter(profile_id=profileId)
        CommentedTopics = CommentedTopic.objects.filter(profile_id=profileId)
        ViewedTopics = ViewedTopic.objects.filter(profile_id=profileId)
    if _platform == "win32":
        return render(request, 'MeancoApp\TopicMap.html' , {'topics': topics,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags,'FollowedTopics':FollowedTopics,'CommentedTopics':CommentedTopics,'ViewedTopics':ViewedTopics, 'id': id})
    else:
        return render(request, 'MeancoApp/TopicMap.html', {'topics': topics,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags,'FollowedTopics':FollowedTopics,'CommentedTopics':CommentedTopics,'ViewedTopics':ViewedTopics, 'id': id})

def get_signup_page(request):
    form = UserCreateForm()
    return render(request, 'signup.html', {'form': form})