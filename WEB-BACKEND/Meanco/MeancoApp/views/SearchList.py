from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.functions.search import *
from ..models import Topic, Tag

def get_page(request):
    oftopics = []
    param = request.GET.get('param')
    action = request.GET.get('action')
    TrendingTopics=Topic.objects.order_by("-view_count")[:7]
    TrendingTags = Tag.objects.order_by("-topic_count")[:7]
    if action == "topic" and param != "":
        topics1 = findStringMatchedTopics(param)
        topics2 = findMutuallyTaggedTopics(param)
        topics3 = findRefTopics(param)
        topics = topics1
        for i in topics2:
            if i not in topics:
                topics.append(i)
        for i in topics3:
            if i not in topics:
                topics.append(i)
        results = Topic.objects.filter(id__in=topics).order_by('-comment_count', '-timestamp_last', '-view_count')

        for t in results:
            oftopics.append(OfTopic.objects.filter(topic=t.id))

    elif action == "tag":   # Tag
        results = Tag.objects.filter(label__startswith=param).order_by('-view_count', '-topic_count')

        for t in results:
            oftopics.append(OfTopic.objects.filter(tag=t.id))

    else:
        results = Topic.objects.all()

    followedTopics = []
    if request.user.is_authenticated:
        profileId = Profile.objects.get(user_id=request.user.id).id
        fTopics = FollowedTopic.objects.filter(profile_id=profileId)
        for ft in fTopics:
            followedTopics.append(ft.topic)

    if _platform=="win32":
        return render(request, 'MeancoApp\SearchList.html', {'results': results,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'action': action, 'followedTopics': followedTopics, 'oftopics': oftopics})
    else:
        return render(request, 'MeancoApp/SearchList.html' , {'results': results,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'action': action, 'followedTopics': followedTopics, 'oftopics': oftopics})