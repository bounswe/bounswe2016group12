from django.shortcuts import render
from sys import platform as _platform
from MeancoApp.models import *
from django.db.models import Q

def get_page(request,id):
    print(id)
    Userid = request.user.id
    relation=Relation.objects.get(id=id)
    topic_a=relation.topic_a.id
    topic_b=relation.topic_b.id
    TrendingTopics=Topic.objects.order_by("-view_count")[:7]
    TrendingTags = Tag.objects.order_by("-topic_count")[:7]
    if(topic_a>topic_b):
        temp=topic_a
        topic_a=topic_b
        topic_b=temp
    relations= Relation.objects.filter(Q(topic_a_id=topic_a,topic_b_id=topic_b) | Q(topic_a_id=topic_b,topic_b_id=topic_a,isBidirectional=False)).order_by("-vote_count")
    topic_a=Topic.objects.get(id=topic_a)
    topic_b=Topic.objects.get(id=topic_b)
    if _platform == "win32":
        return render(request, 'MeancoApp\RelationView.html' , {'relations': relations,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'id': id,'topic_a':topic_a,'topic_b':topic_b})
    else:
        return render(request, 'MeancoApp/RelationView.html', {'relations': relations,'TrendingTopics':TrendingTopics,'TrendingTags':TrendingTags, 'id': Userid,'topic_a':topic_a,'topic_b':topic_b})