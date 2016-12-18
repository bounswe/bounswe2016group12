from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.functions.search import *
from ..models import Topic, Tag

def get_page(request):

    param = request.GET.get('param')
    action = request.GET.get('action')
    TrendingTopics=Topic.objects.order_by("-view_count")[:7]
    TrendingTags = Tag.objects.order_by("-topic_count")[:7]
    if action == "topic":
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
    else:   # Tag
        results = Tag.objects.filter(label__startswith=param).order_by('-view_count', '-topic_count')

    if _platform=="win32":
        return render(request, 'MeancoApp\SearchList.html', {'results': results,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'action': action})
    else:
        return render(request, 'MeancoApp/SearchList.html' , {'results': results,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'action': action})